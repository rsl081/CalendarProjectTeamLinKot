package com.example.calendarprojectteamlinkot.utils

import android.content.SharedPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Constants {
    const val BASE_URL: String = "http://192.168.100.40:60001/api/"

    val id: String = ""
    val USERNAME: String = ""
    val description: String = ""
    val date: String = ""
    val dateCreated: String = ""
    val isCompleted: Boolean = false
    val assignee: String = ""
    val createdBy: String = ""

    const val TASK_DETAIL: String = "task_detail"

    const val TOKEN_USER_MODEL = "token_user_model"

    const val TOKEN_NOTIFICATION = "token_notification"

    lateinit var MSHAREDPREFERENCES: SharedPreferences

    const val PREFERENCE_NAME = "MeTaskAppPreference"
}