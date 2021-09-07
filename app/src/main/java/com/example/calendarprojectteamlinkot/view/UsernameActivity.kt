package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.repository.ApiClass
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_username.*
import android.app.Activity
import android.widget.Toast
import com.example.calendarprojectteamlinkot.utils.Constants


class UsernameActivity: BaseActivity() {

    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        init()
    }

    private fun init() {
        tv_username_taken.visibility = View.INVISIBLE

        btn_next.setOnClickListener {
            username = et_username.text.toString().trim{ it <= ' '}

            if(validateUser(username)) {
                val intent = Intent(this, PasswordActivity::class.java)
                intent.putExtra(Constants.USERNAME, username)
                startActivity(intent)

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        btn_cancel_username.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun validateUser(email: String): Boolean {
        return when{
            TextUtils.isEmpty(email)->{
                Toast.makeText(this,
                    "Please enter a username", Toast.LENGTH_SHORT).show()
                false
            }else->{
                true
            }
        }
    }//end of validateForm

    override fun onResume() {
        et_username.setText(username)
        super.onResume()
    }

}