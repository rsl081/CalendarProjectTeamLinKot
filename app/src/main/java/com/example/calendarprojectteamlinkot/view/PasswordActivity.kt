package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_password.*
import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.widget.Toast
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_username.*
import java.util.regex.Pattern

class PasswordActivity: BaseActivity() {

    var username: String = ""
    var password: String = ""
    var confirmPassword: String = ""
    val pattern_password = Regex("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        username = intent.getStringExtra(Constants.USERNAME).toString()

        btn_back.setOnClickListener {
            onBackPressed()

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        btn_cancel_password.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)

            startActivity(intent)
        }

        btn_submit.setOnClickListener {
            signUpPassword()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun signUpPassword(){
        username = intent.getStringExtra(Constants.USERNAME).toString()
        password = et_password.text.toString().trim{ it <= ' '}
        confirmPassword = et_confirm_password.text.toString().trim{ it <= ' '}

        if(validatePassword(password, confirmPassword))
        {
            //Register from api then catch token
            ApiClass().registerUsernameAndPasswordFromApi(this, username, password)
        }
    }

    private fun validatePassword(password: String, confirmPassword: String,): Boolean {
        return when{
            !TextUtils.equals(password,confirmPassword)->{
                showErrorSnackBar("Password and confirm password did not match")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a confirm password")
                false
            }
            TextUtils.isEmpty(confirmPassword)->{
                showErrorSnackBar("Please enter a password")
                false
            }
            !Pattern.matches(pattern_password.toString(), password) || !Pattern.matches(pattern_password.toString(), confirmPassword) ->{
                showErrorSnackBar("Please enter a mali ihh")
                Toast.makeText(this, "must not be empty\nmust contain at least eight characters long\nmust contain uppercase and lowercase letters and a number", Toast.LENGTH_SHORT).show()
                false
            }else->{
                true
            }
        }
    }//end of validateForm

}