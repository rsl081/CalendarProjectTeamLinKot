package com.example.calendarprojectteamlinkot.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.adapters.TaskListItemsAdapter
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

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    var isToggle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDateCalendar()

        init()

        // FCM token setup
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d(TAG + "Token", token.toString())
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

        val dateToday = findViewById<TextView>(R.id.date_today)
        dateToday.text = displayCurrentDate()

        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

//            val loginResponseCall: Call<Task>? =
//                ApiClass().getUserServiceHeader()?.toggleTaskComplete("Bearer "+msharedToken!!,"1ca4451e-2869-48f0-9b25-4e53a18053f6")

            val loginResponseCall: Call<Task>? =
                ApiClass().getUserServiceHeader()?.toggleTaskComplete("Bearer "+msharedToken!!,"dbc260bd-a7ef-4836-96a4-54627f815f1e")


            loginResponseCall?.enqueue(object: Callback<Task> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Task>, response: Response<Task>) {

                    if(response.isSuccessful) {

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

    }

    private fun init(){

        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        setupActionBar(this, toolbar_main_activity)

        tv_select_task_date.text = displayCurrentDate()

        ApiClass().myTask(this,showDate(year,month,day))
        Log.i("datess", "date $year,$month,$day")

        ApiClass().getCurrentUser{
            tv_name_activtyday.text = "Hi! $it"
        }

        ApiClass().countTaskOfCurrentUser({
            tv_num_of_task_activityday.text = "You have $it tasks today"
        },displayCurrentDate())

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

        toggleButton!!.setOnCheckedChangeListener { _, isChecked ->
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
            R.id.nav_my_tasks -> {
                activity_day.visibility = View.INVISIBLE
               activity_task.visibility = View.VISIBLE
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
//        hour = cal.get(Calendar.HOUR)
//        minute = cal.get(Calendar.HOUR)
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

        //Yung pag click ng date okay naaaaaaaaaaaaaaaaaaaaaaaaa
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
