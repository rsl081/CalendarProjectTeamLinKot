package com.example.calendarprojectteamlinkot.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification (
    val token: String
): Parcelable