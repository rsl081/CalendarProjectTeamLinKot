package com.example.calendarprojectteamlinkot.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class User (
//    val id: String? = "",
    val username: String? = "",
    val token: String? = "",
    val image: String? = "",
//    val task: ArrayList<Task>? = ArrayList(),
): Parcelable