package com.example.calendarprojectteamlinkot.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.repository.ApiServices
import com.example.calendarprojectteamlinkot.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            //if aleady signin punta na agad siya sa main acitivty
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btn_sign_in.setOnClickListener {
            signInRegisteredUser()
        }

        tv_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }


    private fun signInRegisteredUser()
    {
        val email: String = et_username_signIn.text.toString().trim{ it <= ' '}
        val password: String = et_password_sign_in.text.toString().trim{ it <= ' '}

        if(validateForm(email, password))
        {
            showProgressDialog(resources.getString(R.string.please_wait))
            ApiClass().signinFromApi(this,email,password)

        }
    }

    fun proceedToNextAct(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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