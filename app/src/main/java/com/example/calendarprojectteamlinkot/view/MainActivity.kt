package com.example.calendarprojectteamlinkot.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import androidx.core.view.GravityCompat
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.SimpleDateFormat
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

    var isToggle: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        ApiClass().myTask(this)

        toggleButton!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                ApiClass().showAllTask(this)
                isToggle = !isToggle
            } else {
                ApiClass().myTask(this)
                isToggle = !isToggle
            }
        }

        val calendar = Calendar.getInstance()
        val currentDate0 = SimpleDateFormat("EEE, MMM d, yyyy")
        val currentDate1 = SimpleDateFormat("yyyy-MM-dd")

        val tvSelectTaskDate: String = currentDate0.format(calendar.time)

        tv_select_task_date.text = tvSelectTaskDate

        ib_next.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            val tvSelectTaskDate: String = currentDate0.format(calendar.time)
            val tvSelectTaskDate1: String = currentDate1.format(calendar.time)

            tv_select_task_date.text = tvSelectTaskDate

            if(isToggle){
                ApiClass().getAllTaskByDate(this, tvSelectTaskDate1)
            }else{
                ApiClass().getMyTaskByDate(this, tvSelectTaskDate1)
            }
        }

        ib_previous.setOnClickListener {


            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val tvSelectTaskDate: String = currentDate0.format(calendar.time)
            val tvSelectTaskDate1: String = currentDate1.format(calendar.time)

            tv_select_task_date.text = tvSelectTaskDate

            if(isToggle){
                ApiClass().getAllTaskByDate(this, tvSelectTaskDate1)
            }else{
                ApiClass().getMyTaskByDate(this, tvSelectTaskDate1)
            }
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

        val simpledateformat0 = SimpleDateFormat("EEE, MMM d, yyyy")
        val simpledateformat1 = SimpleDateFormat("yyyy-MM-dd")

        val newDate = Calendar.getInstance()
        newDate[savedYear, savedMonth] = savedDay

        val selectedDate0: String = simpledateformat0.format(newDate.time)
        val selectedDate1: String = simpledateformat1.format(newDate.time)

        Log.i("checkedToggle", selectedDate1.toString())
        if(isToggle){
            ApiClass().getAllTaskByDate(this, selectedDate1)
        }else{
            ApiClass().getMyTaskByDate(this, selectedDate1)
        }

        tv_select_task_date.text = selectedDate0

        ib_next.setOnClickListener {
            newDate.add(Calendar.DAY_OF_MONTH, 1)

            val tvSelectTaskDate: String = simpledateformat0.format(newDate.time)

            tv_select_task_date.text = tvSelectTaskDate
            if(isToggle){
                ApiClass().getAllTaskByDate(this, selectedDate1)
            }else{
                ApiClass().getMyTaskByDate(this, selectedDate1)
            }

        }

        ib_previous.setOnClickListener {
            newDate.add(Calendar.DAY_OF_MONTH, -1)

            val tvSelectTaskDate: String = simpledateformat0.format(newDate.time)

            tv_select_task_date.text = tvSelectTaskDate
            if(isToggle){
                ApiClass().getAllTaskByDate(this, selectedDate1)
            }else{
                ApiClass().getMyTaskByDate(this, selectedDate1)
            }
        }
    }
}
