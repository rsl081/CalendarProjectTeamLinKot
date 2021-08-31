package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.Register
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.repository.ApiServices
import com.example.calendarprojectteamlinkot.utils.Constants
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_progress.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : BaseActivity() {

    var username: String = ""
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupActionBar()
        init()
    }//end of onCreate

    private fun init()
    {
        btn_next_register.isEnabled = false
        tv_usernameAlreadyTaken.visibility = View.INVISIBLE
        tv_errorMessage.visibility = View.INVISIBLE

        et_username_register.addTextChangedListener {
            btn_next_register.isEnabled = true
        }

        btn_next_register.setOnClickListener {
            signUpUsername()
        }

        btn_submit.setOnClickListener {
            signUpPassword()
        }
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_not_interested_24)
        }

        toolbar_register_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun signUpUsername() {
        username = et_username_register.text.toString().trim{ it <= ' '}

        if(validateUser(username))
        {
            register_page1.visibility = View.INVISIBLE
            register_page2.visibility = View.VISIBLE
        }

    }//end of signUpRegistered

    private fun validateUser(email: String): Boolean {
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an username")
                false
            }else->{
                true
            }
        }
    }//end of validateForm

    private fun signUpPassword(){
        password= et_password_register.text.toString().trim{ it <= ' '}
        val confirmpassowrd: String = et_confirmpassword_register.text.toString().trim{ it <= ' '}

        if(validatePassword(password, confirmpassowrd))
        {
            //Register from api then catch token
            ApiClass().registerUsernameAndPasswordFromApi(this,username,password)
        }
    }


    private fun validatePassword(password: String, confirmpassowrd: String,): Boolean {
        return when{
            !TextUtils.equals(password,confirmpassowrd)->{
                showErrorSnackBar("Password and confirm password did not match")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter an confirmpassowrd")
                false
            }
            TextUtils.isEmpty(confirmpassowrd)->{
                showErrorSnackBar("Please enter a password")
                false
            }else->{
                true
            }
        }
    }//end of validateForm


    fun proceedToNextAct(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}