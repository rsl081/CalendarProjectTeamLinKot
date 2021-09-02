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
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.adapters.TaskListItemsAdapter
import com.example.calendarprojectteamlinkot.databinding.ActivityTaskBinding
import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.repository.ApiClass
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
import java.text.DateFormat
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

        ApiClass().myTask(this)

        toggleButton!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                ApiClass().showAllTask(this)
            } else {
                ApiClass().myTask(this)
            }
        }

        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("EEE, MMM d, yyyy")

        val tvSelectTaskDate: String = currentDate.format(calendar.time)

        tv_select_task_date.text = tvSelectTaskDate

        ib_next.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            val tvSelectTaskDate: String = currentDate.format(calendar.time)

            tv_select_task_date.text = tvSelectTaskDate
        }

        ib_previous.setOnClickListener {


            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val tvSelectTaskDate: String = currentDate.format(calendar.time)

            tv_select_task_date.text = tvSelectTaskDate
        }
    }

    private fun init(){

        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        setupActionBar(this, toolbar_main_activity)

        ApiClass().getCurrentUser{
            tv_name_activtyday.text = "Hi! $it"
        }

        ApiClass().countTaskOfCurrentUser{
            tv_num_of_task_activityday.text = "You have $it tasks today"
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

        val simpledateformat = SimpleDateFormat("EEE, MMM d, yyyy")
        val newDate = Calendar.getInstance()
        newDate[savedYear, savedMonth] = savedDay

        val selectedDate: String = simpledateformat.format(newDate.time)

        tv_select_task_date.text = selectedDate

        ib_next.setOnClickListener {
            newDate.add(Calendar.DAY_OF_MONTH, 1)

            val tvSelectTaskDate: String = simpledateformat.format(newDate.time)

            tv_select_task_date.text = tvSelectTaskDate
        }

        ib_previous.setOnClickListener {
            newDate.add(Calendar.DAY_OF_MONTH, -1)

            val tvSelectTaskDate: String = simpledateformat.format(newDate.time)

            tv_select_task_date.text = tvSelectTaskDate
        }
    }


}
