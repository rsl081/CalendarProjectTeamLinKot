package com.example.calendarprojectteamlinkot.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditTask(
    val id: String?,
    val name: String?,
    val description: String?,
    val assignee: User,
    val date: String?
): Parcelable