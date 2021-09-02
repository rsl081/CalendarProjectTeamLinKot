package com.example.calendarprojectteamlinkot.repository


import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Register
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiServices {
    @Headers("Content-Type: application/json")
    @GET("tasks")
    fun getTask(
        @Header("Authorization") auth: String,
        @Query("MyTasks") myTask: Boolean): Call<List<Task>>

    @Headers("Content-Type: application/json")
    @POST("Account/login")
    open fun login(@Body login: Login): Call<User>

    @Headers("Accept: application/json")
    @POST("Account/register")
    open fun register(@Body register: Register): Call<User>

    @Headers("Content-Type: application/json")
    @GET("Account")
    fun getCurrentUser(@Header("Authorization") auth: String): Call<User>


}
