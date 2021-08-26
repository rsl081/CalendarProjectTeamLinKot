package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSignIn.setOnClickListener {
            signInRegisteredUser();
        }
    }

    private fun signInRegisteredUser()
    {
        val email: String = et_username_signIn.text.toString().trim{ it <= ' '}
        val password: String = et_password_signIn.text.toString().trim{ it <= ' '}

        if(validateForm(email, password))
        {
            showProgressDialog(resources.getString(R.string.please_wait))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an email")
                false
            }
                TextUtils.isEmpty(password)->{
                    showErrorSnackBar("Please enter a password")
                    false
            }else->{
                true
            }
        }
    }//end of validateForm

}