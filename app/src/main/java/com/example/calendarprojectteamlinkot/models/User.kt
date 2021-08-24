package com.example.calendarprojectteamlinkot.models

data class User (
    val id: String? = "",
    val username: String? = "",
    val image: String? = "",
    val task: ArrayList<Task>? = ArrayList(),
    val token: String? = "",
)