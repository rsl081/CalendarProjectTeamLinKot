package com.example.calendarprojectteamlinkot.models

import java.io.Serializable

data class Login (
    val username: String? = "",
    val password: String? = "",
): Serializable