package com.example.calendarprojectteamlinkot.repository


import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Register
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("tasks")
    fun getData(): Call<List<Task>>

    @POST("Account/login")
    open fun login(@Body login: Login): Call<User>

    @Headers("Accept: application/json")
    @POST("Account/register")
    open fun register(@Body register: Register): Call<User>

}
