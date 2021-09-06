package com.example.calendarprojectteamlinkot.models

import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class CreateTask(
    val name: String?,
    val description: String?,
    val assignee: User,
    val date: String?
): Parcelable