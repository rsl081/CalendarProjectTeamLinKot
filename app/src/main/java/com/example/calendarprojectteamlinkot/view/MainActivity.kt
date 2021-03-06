package com.example.calendarprojectteamlinkot.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.adapters.TaskListItemsAdapter
import com.example.calendarprojectteamlinkot.models.Notification
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import com.example.calendarprojectteamlinkot.utils.MyFirebaseMessagingService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    var username: String? = null

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0

    private var isToggle: Boolean = false

    lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDateCalendar()

        init()

        val dateToday = findViewById<TextView>(R.id.date_today)
        dateToday.text = displayCurrentDate()

        this.mHandler = Handler()

        this.mHandler.postDelayed(m_Runnable, 5000)
    }

    private val m_Runnable: Runnable = object : Runnable {
        override fun run() {
            ApiClass().countTaskOfCurrentUser({
                if (it != null) {
                    when (it) {
                        1 -> {
                            tv_num_of_task_activity_day.text = "You have $it task for today."
                        }
                        0 -> {
                            tv_num_of_task_activity_day.text = "You have no task for today."
                        }
                        else -> {
                            tv_num_of_task_activity_day.text = "You have $it tasks today."
                        }
                    }
                }
            },displayCurrentDate())
            this@MainActivity.mHandler.postDelayed(this, 5000)
        }
    } //runnable

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(m_Runnable)
        finish()
    }

    private fun init(){

        // FCM token setup
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result


            Log.d(TAG + "Token", token.toString())

            if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
                Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)


                val editor = Constants.MSHAREDPREFERENCES.edit()
                editor.putString(Constants.TOKEN_NOTIFICATION, token)
                editor.apply()

                val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")
                val notiftoken = Notification(token.toString())
                val loginResponseCall: Call<Task>? =
                    notiftoken?.let {
                        ApiClass().getUserServiceHeader()?.token_register("Bearer "+msharedToken!!,
                            it
                        )
                    }

                loginResponseCall?.enqueue(object: Callback<Task> {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onResponse(call: Call<Task>, response: Response<Task>) {

                        if(response.isSuccessful) {
                            Log.i("Token_Register", "NICEEEEE")
                        }else{
                            val rc =  response.code()
                            when(rc){
                                400->{
                                    Log.e("Error 400 showAllTask", "Bad Request")
                                }
                                403-> {
                                    Log.e("Error 403", "Not Found" + rc)
                                }else ->{
                                Log.e("Error", "Happy Generic Error" + rc)
                            }
                            }
                        }
                    }
                    override fun onFailure(call: Call<Task>, t: Throwable) {
                        Log.e("Errorrrrr", t!!.message.toString())
                    }
                })
            }
        })


        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        setupActionBar(this, toolbar_main_activity)

        tv_select_task_date.text = displayCurrentDate()

        ApiClass().myTask(this,showDate(year,month,day))
        Log.i("datess", "date $year,$month,$day")

        ApiClass().getCurrentUser{
            tv_name_activity_day.text = "Hi! $it"
        }

        nav_view.setNavigationItemSelectedListener(this)

        nav_sign_out.setOnClickListener {
            ApiClass().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        tv_select_task_date.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(this, this,year,month,day).show()
        }

        tb_my_task_show_all!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                ApiClass().showAllTask(this, showDate(year,month,day))
                isToggle = !isToggle
            } else {
                ApiClass().myTask(this, showDate(year,month,day))
                isToggle = !isToggle
            }
        }

        ib_next.setOnClickListener {

            tv_select_task_date.text = dayOfMonth(savedYear,savedMonth,++savedDay)

            if(isToggle){
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getAllTaskByDate(this, showDate(year,month,++day))
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getMyTaskByDate(this, showDate(year,month,++day))
            }
        }

        ib_previous.setOnClickListener {

            tv_select_task_date.text = dayOfMonth(savedYear,savedMonth,--savedDay)

            if(isToggle){
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getAllTaskByDate(this, showDate(year,month,--day))
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getMyTaskByDate(this, showDate(year,month,--day))
            }
        }

        fab_create_task.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        refreshLayout.setOnRefreshListener {
            if(isToggle){
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getAllTaskByDate(this, showDate(year,month,day))
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getMyTaskByDate(this, showDate(year,month,day))
            }
        }

        calendar_view.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            val simpledateformat = SimpleDateFormat("yyyy-MM-dd")
            val simpledateformat1 = SimpleDateFormat("EEE, MMM d, yyyy")

            val newDate = Calendar.getInstance()
            newDate[year, month] = dayOfMonth

            val selectedDate: String = simpledateformat.format(newDate.time)
            val selectedDate1: String = simpledateformat1.format(newDate.time)

            activity_day.visibility = View.INVISIBLE
            activity_task.visibility = View.VISIBLE

            if(isToggle){
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getAllTaskByDate(this, selectedDate)
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                ApiClass().getMyTaskByDate(this, selectedDate)
            }

            tv_select_task_date.text = selectedDate1
        }

    }//end of init

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_calendar ->{
                activity_task.visibility = View.INVISIBLE
                activity_day.visibility = View.VISIBLE
            }
            R.id.nav_my_tasks ->{
                activity_day.visibility = View.INVISIBLE
                activity_task.visibility = View.VISIBLE
            }
            R.id.nav_settings ->{
                startActivity(Intent(this, ChangePassActivity::class.java))
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun getDateCalendar(){
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

        savedDay = cal.get(Calendar.DAY_OF_MONTH)
        savedMonth = cal.get(Calendar.MONTH)
        savedYear = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        this.day = dayOfMonth
        this.month = month
        this.year = year

        val simpledateformat0 = SimpleDateFormat("EEE, MMM d, yyyy")
        val simpledateformat1 = SimpleDateFormat("yyyy-MM-dd")

        val newDate = Calendar.getInstance()
        newDate[savedYear, savedMonth] = savedDay

        val selectedDate0: String = simpledateformat0.format(newDate.time)
        val selectedDate1: String = simpledateformat1.format(newDate.time)

        if(isToggle){
            showProgressDialog(resources.getString(R.string.please_wait))
            ApiClass().getAllTaskByDate(this, selectedDate1)
        }else{
            showProgressDialog(resources.getString(R.string.please_wait))
            ApiClass().getMyTaskByDate(this, selectedDate1)
        }

        tv_select_task_date.text = selectedDate0

    }

}
