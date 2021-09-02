package com.example.calendarprojectteamlinkot.models

data class Task (
    val id: String?,
    val name: String?,
    val description: String?,
    val createdBy: User,
    val assignee: User?,
    var date: String?,
    val dateCreated: String?,
    val isCompleted: Boolean?,
    var expand : Boolean = false
)