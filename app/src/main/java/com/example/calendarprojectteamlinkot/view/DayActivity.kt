package com.example.calendarprojectteamlinkot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_day.*

class DayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)


        setSupportActionBar(toolbar_day_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        toolbar_day_activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}