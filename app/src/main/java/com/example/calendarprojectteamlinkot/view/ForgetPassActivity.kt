package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_forget_pass.*

class ForgetPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        btn_send.setOnClickListener {
            val intent = Intent(this, VerificationActivity::class.java)

            startActivity(intent)
        }

    }
}