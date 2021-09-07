package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        btn_back.setOnClickListener {
            val intent = Intent(this, UsernameActivity::class.java)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}