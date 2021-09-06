package com.example.calendarprojectteamlinkot.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.CreateTask
import com.example.calendarprojectteamlinkot.models.EditTask
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import kotlinx.android.synthetic.main.activity_create_task.*
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    var etName: String = ""
    var etDescription: String = ""
    var etDate: String = ""
    var etAssignee: String = ""

    var day = 0
    var month = 0
    var year = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        init()



    }


    private fun init(){

        setupActionBar()

        ApiClass().getAllUser(this)

        if(intent.hasExtra(Constants.TASK_DETAIL)){
            val editTask = intent.getParcelableExtra<EditTask>(Constants.TASK_DETAIL)
            val username = editTask?.assignee
            et_task_name.setText(editTask?.name)
            et_task_description.setText(editTask?.description)
            et_task_date.setText(editTask?.date)
            ac_assignee.setText(username?.username)

            btn_create.setOnClickListener{
                etName = et_task_name.text.toString()
                etDescription = et_task_description.text.toString()
                etDate = et_task_date.text.toString()
                etAssignee = ac_assignee.text.toString()
                val usr = User(etAssignee)
                if(validateForm(etName,etDescription,etDate,etAssignee)){
                    val editTask = EditTask(editTask?.id,etName,etDescription,usr,etDate)

                    ApiClass().editTask(this, editTask?.id, editTask)
                }
            }

        }else{
            btn_create.setOnClickListener{
                etName = et_task_name.text.toString()
                etDescription = et_task_description.text.toString()
                etDate = et_task_date.text.toString()
                etAssignee = ac_assignee.text.toString()
                val usr = User(etAssignee)
                if(validateForm(etName,etDescription,etDate,etAssignee)){
                    val createTask = CreateTask(etName,etDescription,usr,etDate)

                    ApiClass().createTasK(this, createTask)
                }
            }
        }



        tv_cancel_button.setOnClickListener {
            onBackPressed()
        }

        et_task_date.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(this, this,year,month,day).show()
        }
    }

    fun setupActionBar(){
        setSupportActionBar(tb_create_task)
        tb_create_task.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        tb_create_task.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun validateForm(name: String, description: String, date: String, assignee: String): Boolean {
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(description)->{
                showErrorSnackBar("Please enter a description")
                false
            }TextUtils.isEmpty(date)->{
                showErrorSnackBar("Please enter a date")
                false
            }TextUtils.isEmpty(assignee)->{
                showErrorSnackBar("Please enter a assignee")
                false
            }else->{
                true
            }
        }
    }//end of validateForm


    private fun getDateCalendar(){
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month
        this.year = year

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = Calendar.getInstance()
        newDate[year, month] = day

        val selectedDate: String = sdf.format(newDate.time)

        et_task_date.setText(selectedDate)
    }

}