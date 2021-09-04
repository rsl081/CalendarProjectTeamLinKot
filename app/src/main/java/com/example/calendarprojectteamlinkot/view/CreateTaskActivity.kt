package com.example.calendarprojectteamlinkot.view

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.CreateTask
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import kotlinx.android.synthetic.main.activity_create_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        val loginResponseCall: Call<List<User>>? =
            ApiClass().getUserServiceHeader()?.getAllUser()

        loginResponseCall?.enqueue(object : Callback<List<User>> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
//                    val ctr = response.body()?
//                    for(happy in ctr){
//                        c
//                    }

                } else {
                    val rc = response.code()
                    Log.e("Error", "Error " + rc)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("Errorrrrr", t!!.message.toString())
            }
        })

        btn_create.setOnClickListener{
            etName = et_task_name.text.toString()
            etDescription = et_task_description.text.toString()
            etAssignee = et_task_assignee.text.toString()



            val usr = User("genesis")

            Log.i("CreateTask", "$etName, $etDescription, $etAssignee, $etDate")

            val createTask = CreateTask(etName,etDescription,usr,etDate)

            ApiClass().createTasK(this, createTask)
        }

        et_task_date.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(this, this,year,month,day).show()
        }

    }

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