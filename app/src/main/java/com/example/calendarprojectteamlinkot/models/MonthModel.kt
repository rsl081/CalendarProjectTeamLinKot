package com.example.calendarprojectteamlinkot.models

import com.example.calendarprojectteamlinkot.enums.Month
import com.example.calendarprojectteamlinkot.view.MonthAdapter

data class MonthModel (var id:Int, var year: Int, var days: ArrayList<Int>,
                       var week: ArrayList<String>, var task: String, var isSelected: Boolean,
                       var isCompleted: Boolean, var month: Month)

//data class KalendarModel (var id:Int, var year: ArrayList<Int>, var days: ArrayList<Int>,
//                          var week: ArrayList<String>, var task: String, var isSelected: Boolean,
//                          var isCompleted: Boolean,var month: Month)


//data class Task (var task:String)
