package com.example.calendarprojectteamlinkot.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Register
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.utils.Constants
import com.example.calendarprojectteamlinkot.view.RegisterActivity
import com.example.calendarprojectteamlinkot.view.SignInActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClass {



    fun registerUsernameAndPasswordFromApi(username: String, password: String)
    {
        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val services = retrofit.create(ApiServices::class.java)

        val register = Register(username,password)
        val listCall = services.register(register)

        listCall.enqueue(object: Callback<User> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    val token  = response.body()?.token
                    Log.i("Response1", "$token")
                }else{
                    val rc =  response.code()
                    when(rc){
                        400->{
                            Log.e("Error 400", "Bad Connection Username taken")
                        }
                        404-> {
                            Log.e("Error 404", "Not Found")
                        }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Error", t!!.message.toString())
                //hideProgressDialog()
            }
        })
    }//end of getUsernameFromApi

    fun getCurrentUser()
    {
        //dito yung signout

    }

    fun listTaskOfCurrentUser()
    {

    }

    fun listAllTask()
    {

    }

    fun createTasK()
    {
        //User only to
    }

}