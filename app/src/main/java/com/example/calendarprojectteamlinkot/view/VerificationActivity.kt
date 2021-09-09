package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_forget_pass.*
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        btn_verify.setOnClickListener {
            val intent = Intent(this, ChangePassActivity::class.java)

            startActivity(intent)

        }
    }
}