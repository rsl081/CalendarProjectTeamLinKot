package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiServices
import com.example.calendarprojectteamlinkot.utils.Constants
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

        btnSignIn.setOnClickListener {
            signInRegisteredUser();
        }

        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val services = retrofit.create(ApiServices::class.java)


        val login = Login("raph","Pa\$\$w0rd")
        val listCall = services.login(login)

        listCall.enqueue(object: Callback<User> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {

                    val token  = response.body()?.token
                    Log.i("Response1", "$token")
                }else{
                    val rc =  response.code()
                    when(rc){
                        400->{
                            Log.e("Error 400", "Bad Connection")
                        }
                        404-> {
                            Log.e("Error 404", "Not Found")
                        }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Errorrrrr", t!!.message.toString())
                //hideProgressDialog()
            }
        })
    }

    private fun signInRegisteredUser()
    {
        val email: String = et_username_sigin.text.toString().trim{ it <= ' '}
        val password: String = et_password_sigin.text.toString().trim{ it <= ' '}

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
                showErrorSnackBar("Please enter a email")
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