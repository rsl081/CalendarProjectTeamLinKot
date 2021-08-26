package com.example.calendarprojectteamlinkot.repository


import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
    @GET("tasks")
    fun getData(): Call<List<Task>>

    @POST("Account/login")
    open fun login(@Body login: Login): Call<User>

}
