package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_username.*

class UsernameActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        btn_confirm.setOnClickListener {
            val intent = Intent(this, PasswordActivity::class.java)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
}