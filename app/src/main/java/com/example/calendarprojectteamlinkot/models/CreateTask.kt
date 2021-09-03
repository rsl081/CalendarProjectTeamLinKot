package com.example.calendarprojectteamlinkot.models

data class CreateTask (
    val name: String,
    val description: String,
    val assignee: User,
    val date: String
)