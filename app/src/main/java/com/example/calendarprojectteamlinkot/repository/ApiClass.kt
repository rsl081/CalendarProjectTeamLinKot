package com.example.calendarprojectteamlinkot.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.calendarprojectteamlinkot.models.Login
import com.example.calendarprojectteamlinkot.models.Register
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.utils.Constants
import com.example.calendarprojectteamlinkot.view.RegisterActivity
import com.example.calendarprojectteamlinkot.view.SignInActivity
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClass: Interceptor {

    private fun getRetrofit(): Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient =
            OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    fun getUserService(): ApiServices? {
        return getRetrofit().create(ApiServices::class.java)
    }

    fun signinFromApi(activity: SignInActivity, username: String, password: String)
    {
        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val services = retrofit.create(ApiServices::class.java)

        val login = Login(username,password)

        val loginResponseCall: Call<User> =
            ApiClass().getUserService()?.login(login)!!

        loginResponseCall.enqueue(object: Callback<User> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {

                    val tokenForSignin  = response.body()

                    val tokenResponseJsonString = Gson().toJson(tokenForSignin)
                    val editor = Constants.MSHAREDPREFERENCES.edit()
                    editor.putString(Constants.TOKEN_USER_MODEL, tokenResponseJsonString)
                    editor.apply()
                    activity.proceedToNextAct()
                    Log.i("Response1", "$tokenResponseJsonString")
                }else{
                    val rc =  response.code()
                    when(rc){
                        400->{
                            Log.e("Error 400", "Bad Request")
                        }
                        404-> {
                            Log.e("Error 404", "Not Found")
                        }else ->{
                            activity.hideProgressDialog()
                            activity.showErrorSnackBar("Mali pre!")
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

    fun registerUsernameAndPasswordFromApi(activity: RegisterActivity, username: String, password: String)
    {
        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val services = retrofit.create(ApiServices::class.java)

        val register = Register(username,password)
        val listCall = services.register(register)

        listCall.enqueue(object: Callback<User> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    val tokenForRegister  = response.body()

                    val tokenResponseJsonString = Gson().toJson(tokenForRegister)
                    val editor = Constants.MSHAREDPREFERENCES.edit()
                    editor.putString(Constants.TOKEN_USER_MODEL, tokenResponseJsonString)
                    editor.apply()
                    activity.proceedToNextAct()

                    Log.i("Response1", "$tokenResponseJsonString")

                }else{
                    val rc =  response.code()
                    when(rc){
                        400->{
                            Log.e("Error 400", "Bad Connection Username taken")
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
                Log.e("Error", t!!.message.toString())
                //hideProgressDialog()
            }
        })
    }//end of getUsernameFromApi

    fun getCurrentUser(): String
    {
        val user: User
        var name = ""
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            val gson = Gson()
            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL,"")
            if (msharedToken != null) {
                val noQuotes: String? = msharedToken?.replace("^\"|\"$", "")
                user = gson.fromJson(noQuotes, User::class.java)
                name = user.username.toString()
            }
        }
        return name
    }

    fun listTaskOfCurrentUser()
    {

    }

    fun listAllTask()
    {

    }

    fun createTasK()
    {
        //User only to
    }

    fun signOut()
    {
        Constants.MSHAREDPREFERENCES.edit().clear().commit()
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        // Get reuqest info
        // Get reuqest info
        val initialReq: Request = chain.request()
        // Create modified request to return
        // Create modified request to return
        val modRequest: Request = initialReq
        // your logic...

        // your logic...
        return chain.proceed(modRequest)
    }

}