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
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.adapters.TaskListItemsAdapter
import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.repository.ApiServices
import com.example.calendarprojectteamlinkot.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.app_bar_main.*
import okhttp3.internal.format
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val services = retrofit.create(ApiServices::class.java)

        val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

        val loginResponseCall: Call<List<Task>>? =
            ApiClass().getUserServiceHeader()?.getTask("Bearer "+msharedToken!!, true)

        loginResponseCall?.enqueue(object: Callback<List<Task>> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if(response.isSuccessful) {

                    val task = response.body()
                    Log.i("MyTask1", task.toString())
                    var adapter: List<Task>
                    if (task != null) {

//                        for(t in task){
//                            val qwe = t.assignee
//                            val name = qwe?.username
//                            if(username?.equals(name)!!){
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                                    val parsedDate = LocalDateTime.parse(t.date, DateTimeFormatter.ISO_DATE_TIME)
//                                    val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy"))
//
//                                    val sdf = format(formattedDate)
//                                    Log.i("MyTask1", sdf)
//
//                                } else {
//                                    TODO("VERSION.SDK_INT < O")
//                                }
//
//                            }
//                        }

                        rv_activity_task.layoutManager = LinearLayoutManager(this@MainActivity)
                        rv_activity_task.setHasFixedSize(true)

                        val adapter = TaskListItemsAdapter(this@MainActivity,
                            task)

                        rv_activity_task.adapter= adapter
                    }
                }else{
                    val rc =  response.code()
                    when(rc){
                        400->{
                            Log.e("Error 400", "Bad Request")
                        }
                        404-> {
                            Log.e("Error 404", "Not Found")
                        }else ->{
                        Log.e("Error", "Generic Error" + rc)
                    }
                    }
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e("Errorrrrr", t!!.message.toString())
                //hideProgressDialog()
            }
        })

    }

    private fun init(){

        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        setupActionBar(this, toolbar_main_activity)

        ApiClass().getCurrentUser{
            tv_name_activtyday.text = "Hi! $it"
        }
       // tv_num_of_task_activityday.text = "You have ${ApiClass().countTaskOfCurrentUser()} tasks today"

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
    }




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
//        hour = cal.get(Calendar.HOUR)
//        minute = cal.get(Calendar.HOUR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        val simpledateformat = SimpleDateFormat("EEEE, d MMMM, yyyy")
        val newDate = Calendar.getInstance()
        newDate[savedYear, savedMonth] = savedDay

        val selectedDate: String = simpledateformat.format(newDate.time)

        tv_select_task_date.text = selectedDate

    }


}
