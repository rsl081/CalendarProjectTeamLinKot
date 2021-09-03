package com.example.calendarprojectteamlinkot.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.CreateTask
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.repository.ApiServices
import com.example.calendarprojectteamlinkot.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_task.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateTaskActivity : AppCompatActivity() {

    var etName: String = ""
    var etDescription: String = ""
    var etDate: String = ""
    var etAssignee: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        btn_create.setOnClickListener{
            etName = et_task_name.text.toString()
            etDescription = et_task_description.text.toString()
            etAssignee = et_task_assignee.text.toString()
            etDate = et_task_date.text.toString()
            val usr = User("genesis")

            val createdtask = CreateTask(etName, etDescription, usr, etDate)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            Log.e("Error 400", createdtask.toString())

            val createTaskResponseCall: Call<Task> =
                ApiClass().getUserServiceHeader()?.createTask("Bearer $msharedToken", createdtask)!!


            createTaskResponseCall.enqueue(object: Callback<Task> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if(response.isSuccessful) {

                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                val qwe =  response.errorBody()?.charStream()?.readText()
                                val jsonObject = JSONObject(qwe!!.trim())
                                var message = jsonObject.getJSONArray("errors")


                                for(i in 0 until message.length()){
                                }


                            }
                            404-> {
                                Log.e("Error 400", "Not Found")
                            }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                        }
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("Error1", t!!.message.toString())
                    //hideProgressDialog()
                }
            })
            Log.i("CreateTask", "$etName, $etDescription, $etAssignee, $etDate")
        }



    }
}