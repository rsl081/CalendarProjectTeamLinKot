package com.example.calendarprojectteamlinkot.repository

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.adapters.TaskListItemsAdapter
import com.example.calendarprojectteamlinkot.models.*
import com.example.calendarprojectteamlinkot.utils.Constants
import com.example.calendarprojectteamlinkot.view.CreateTaskActivity
import com.example.calendarprojectteamlinkot.view.MainActivity
import com.example.calendarprojectteamlinkot.view.RegisterActivity
import com.example.calendarprojectteamlinkot.view.SignInActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.activity_task.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.*
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


    private fun getRetrofitHeader(): Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val okHttpClient =
            OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    fun getUserServiceHeader(): ApiServices? {
        return getRetrofitHeader().create(ApiServices::class.java)
    }

    fun signinFromApi(activity: SignInActivity, username: String, password: String) {
        val login = Login(username,password)

        val loginResponseCall: Call<User> =
            ApiClass().getUserService()?.login(login)!!

        loginResponseCall.enqueue(object: Callback<User> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {

                    val tokenForSignin  = response.body()?.token

                    val tokenResponseJsonString = Gson().toJson(tokenForSignin)
                    val editor = Constants.MSHAREDPREFERENCES.edit()
                    editor.putString(Constants.TOKEN_USER_MODEL, tokenForSignin)
                    editor.apply()
                    activity.proceedToNextAct()

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
                            val qwe =  response.errorBody()?.charStream()?.readText()
                            val jsonObject = JSONObject(qwe!!.trim())
                            var message = jsonObject.getJSONArray("errors")

                            Log.e("ErrorRegister", message.get(1) as String)

                            for(i in 0 until message.length()){
                                Toast.makeText(activity, message.get(i) as String, Toast.LENGTH_SHORT).show();
                            }


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
                Log.e("Error1", t!!.message.toString())
                //hideProgressDialog()
            }
        })
    }//end of getUsernameFromApi

    fun getCurrentUser(userCallback: (String?) -> Unit)
    {
//            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL,"")
//                val noQuotes: String? = msharedToken?.replace("^\"|\"$", "")
        val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

        val loginResponseCall: Call<User>? =
            ApiClass().getUserServiceHeader()?.getCurrentUser("Bearer "+msharedToken!!)

        loginResponseCall?.enqueue(object : Callback<User> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {

                    val username = response.body()?.username
                    userCallback(username)

                } else {
                    val rc = response.code()
                    when (rc) {
                        400 -> {
                            Log.e("Error 400", "Bad Request")
                        }
                        404 -> {
                            Log.e("Error 404", "Not Found")
                        }
                        else -> {
                            Log.e("Error", "Generic Error" + rc)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Errorrrrr", t!!.message.toString())
            }
        })
    }//end of getCurrentUser

    fun countTaskOfCurrentUser(userCallback: (Int?) -> Unit, selectedDate: String)
    {
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)) {
            val msharedToken =
                Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<List<Task>>? =
                ApiClass().getUserServiceHeader()?.getTask("Bearer " + msharedToken!!, true, false,selectedDate)

            loginResponseCall?.enqueue(object : Callback<List<Task>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    if (response.isSuccessful) {
                        val ctr = response.body()?.size
                        userCallback(ctr)
                    } else {
                        val rc = response.code()
                        Log.e("Error", "Error " + rc)
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                }
            })
        }
    }// end of countTaskOfCurrentUser

    fun getAllUser(activitiy: CreateTaskActivity){
        val assignee: ArrayList<String> = ArrayList<String>()

        val loginResponseCall: Call<List<User>>? =
            ApiClass().getUserServiceHeader()?.getAllUser()

        loginResponseCall?.enqueue(object : Callback<List<User>> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val ctr = response.body()

                    if (ctr != null) {
                        for(happy in ctr){
                            happy.username?.let { assignee.add(it) }
                            Log.i("AccountList", happy.username!!)
                        }
                        val arrayApadter = ArrayAdapter(activitiy, R.layout.dropdown_item_create_task, assignee)
                        activitiy.autoComplete_create_task.setAdapter(arrayApadter)
                    }

                } else {
                    val rc = response.code()
                    Log.e("Error", "Error " + rc)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("Errorrrrr", t!!.message.toString())
            }
        })
    }//end of getAllUser

    fun getMyTaskByDate(activity: MainActivity, selectedDate: String){
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<List<Task>>? =
                ApiClass().getUserServiceHeader()?.getMyTaskByDate("Bearer "+msharedToken!!, true, selectedDate)

            loginResponseCall?.enqueue(object: Callback<List<Task>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    if(response.isSuccessful) {
                        activity.hideProgressDialog()
                        val task = response.body()
                        Log.i("MyTask2", task.toString())

                        activity.rv_activity_task.layoutManager = LinearLayoutManager(activity)
                        activity.rv_activity_task.setHasFixedSize(true)

                        val adapter = task?.let { TaskListItemsAdapter(activity, it) }

                        activity.rv_activity_task.adapter= adapter
                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400", "Bad Request")
                            }
                            404-> {
                                Log.e("Error 404", "Not Found")
                            }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                    //hideProgressDialog()
                }
            })
        }
    }

    fun getAllTaskByDate(activity: MainActivity, selectedDate: String){
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<List<Task>>? =
                ApiClass().getUserServiceHeader()?.getAllTaskByDate("Bearer "+msharedToken!!,selectedDate)

            loginResponseCall?.enqueue(object: Callback<List<Task>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    if(response.isSuccessful) {
                        activity.hideProgressDialog()
                        val task = response.body()
                        Log.i("MyTask2", task.toString())

                        activity.rv_activity_task.layoutManager = LinearLayoutManager(activity)
                        activity.rv_activity_task.setHasFixedSize(true)

                        val adapter = task?.let { TaskListItemsAdapter(activity, it) }

                        activity.rv_activity_task.adapter= adapter
                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400", "Bad Request getAllTaskByDate")
                            }
                            404-> {
                                Log.e("Error 404", "Not Found")
                            }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                    //hideProgressDialog()
                }
            })
        }
    }//end getAllTaskByDate

    fun myTask(activity: MainActivity, selectedDate: String) {
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<List<Task>>? =
                ApiClass().getUserServiceHeader()?.getTask("Bearer "+msharedToken!!, true,false,selectedDate)

            loginResponseCall?.enqueue(object: Callback<List<Task>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    if(response.isSuccessful) {

                        val task = response.body()
                        Log.i("MyTask1", task.toString())
                        var adapter: List<Task>
                        if (task != null) {
                            activity.rv_activity_task.layoutManager = LinearLayoutManager(activity)
                            activity.rv_activity_task.setHasFixedSize(true)

                            val adapter = TaskListItemsAdapter(activity,task)

                            activity.rv_activity_task.adapter= adapter
                        }
                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400 myTask", "Bad Request")
                            }
                            404-> {
                                Log.e("Error 404", "Not Found")
                            }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                    //hideProgressDialog()
                }
            })
        }
    }

    fun showAllTask(activity: MainActivity, selectedDate: String)
    {
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<List<Task>>? =
                ApiClass().getUserServiceHeader()?.getTask("Bearer "+msharedToken!!, false,true,selectedDate)

            loginResponseCall?.enqueue(object: Callback<List<Task>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    if(response.isSuccessful) {

                        val task = response.body()
                        Log.i("MyTask1", task.toString())
                        var adapter: List<Task>
                        if (task != null) {
//                                    val parsedDate = LocalDateTime.parse(t.date, DateTimeFormatter.ISO_DATE_TIME)
//                                    val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy"))
//
//                                    val sdf = format(formattedDate)
//                                    Log.i("MyTask1", sdf)

                            activity.rv_activity_task.layoutManager = LinearLayoutManager(activity)
                            activity.rv_activity_task.setHasFixedSize(true)

                            val adapter = TaskListItemsAdapter(activity,
                                task)

                            activity.rv_activity_task.adapter = adapter
                        }
                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400 showAllTask", "Bad Request")
                            }
                            404-> {
                                Log.e("Error 404", "Not Found")
                            }else ->{
                            Log.e("Error", "Generic Error" + rc)
                        }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                    //hideProgressDialog()
                }
            })
        }
    }

    fun createTasK(activity: CreateTaskActivity, createdTask: CreateTask)
    {
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)) {
            val msharedToken =
                Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val createTaskResponseCall: Call<Task> =
                ApiClass().getUserServiceHeader()?.createTask("Bearer $msharedToken", createdTask)!!

            createTaskResponseCall.enqueue(object : Callback<Task> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if (response.isSuccessful) {
                        val rc = response.body()
                        Log.i("Createtask", rc.toString())
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                        activity.finish()
                    } else {
                        val rc = response.code()
                        when (rc) {
                            400 -> {
                                val qwe = response.errorBody()?.charStream()?.readText()
                                val jsonObject = JSONObject(qwe!!.trim())
                                var message = jsonObject.getJSONArray("errors")
                                for (i in 0 until message.length()) {
                                }
                            }
                            404 -> {
                                Log.e("Error 404", "Create task")
                            }
                            else -> {
                                Log.e("Error", "Create task" + rc)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("Error1", t!!.message.toString())
                    activity.hideProgressDialog()
                }
            })
        }
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