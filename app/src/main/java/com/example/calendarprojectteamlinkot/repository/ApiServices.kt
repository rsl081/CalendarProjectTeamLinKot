package com.example.calendarprojectteamlinkot.repository


import com.example.calendarprojectteamlinkot.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiServices {

    @Headers("Content-Type: application/json")
    @POST("Account/login")
    open fun login(@Body login: Login): Call<User>

    @Headers("Accept: application/json")
    @POST("Account/register")
    open fun register(@Body register: Register): Call<User>

    //User
    @Headers("Content-Type: application/json")
    @GET("Account")
    fun getCurrentUser(@Header("Authorization") auth: String): Call<User>

    @Headers("Content-Type: application/json")
    @GET("Account/list")
    fun getAllUser(@Header("Authorization") auth: String): Call<List<User>>

    //Task
    @Headers("Content-Type: application/json")
    @GET("tasks")
    fun getTask(
        @Header("Authorization") auth: String,
        @Query("MyTasks") myTask: Boolean,
        @Query("ShowAll") showAll: Boolean,
        @Query("Date") myDateTask: String,
    ): Call<List<Task>>

    @Headers("Content-Type: application/json")
    @GET("tasks")
    fun getAllTaskByDate(
        @Header("Authorization") auth: String,
        @Query("Date") myTask: String,
    ): Call<List<Task>>

    @Headers("Content-Type: application/json")
    @GET("tasks")
    fun getMyTaskByDate(
        @Header("Authorization") auth: String,
        @Query("MyTasks") myTask: Boolean,
        @Query("Date") myDateTask: String,
    ): Call<List<Task>>

    @Headers("Content-Type: application/json")
    @POST("tasks")
    fun createTask(
        @Header("Authorization") auth: String,
        @Body createTask: CreateTask
    ): Call<Task>

    @Headers("Content-Type: application/json")
    @POST("tasks/{id}/toggle")
    fun toggleTaskComplete(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
    ): Call<Task>

    @Headers("Content-Type: application/json")
    @DELETE("tasks/{id}")
    fun deleteTask(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
    ): Call<Task>

    @Headers("Content-Type: application/json")
    @PUT("tasks/{id}")
    fun editTask(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
        @Body createTask: EditTask
    ): Call<Task>

    @Headers("Content-Type: application/json")
    @POST("Account/tokens/register")
    fun token_register(
        @Header("Authorization") auth: String,
        @Body token: Notification,
    ): Call<Task>

    @Headers("Content-Type: application/json")
//    @DELETE("Account/tokens/delete")
    @HTTP(method = "DELETE", path = "Account/tokens/delete", hasBody = true)
    fun token_delete(
        @Header("Authorization") auth: String,
        @Body token: Notification,
    ): Call<Task>

}
