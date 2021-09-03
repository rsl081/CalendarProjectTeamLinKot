package com.example.calendarprojectteamlinkot.models

import android.widget.EditText

data class CreateTask(
    val name: String,
    val description: String,
    val assignee: User,
    val date: String
)