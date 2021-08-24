package com.example.calendarprojectteamlinkot.models

data class User (
    val id: String? = "",
    val username: String? = "",
    val task: ArrayList<Task>? = ArrayList(),
)