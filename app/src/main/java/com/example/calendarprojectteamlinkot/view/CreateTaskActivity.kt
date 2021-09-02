package com.example.calendarprojectteamlinkot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_task.*
import java.text.DateFormat
import java.util.*

class CreateTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
    }
}