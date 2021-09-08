package com.example.calendarprojectteamlinkot.repository

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.adapters.TaskListItemsAdapter
import com.example.calendarprojectteamlinkot.models.*
import com.example.calendarprojectteamlinkot.utils.Constants
import com.example.calendarprojectteamlinkot.view.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_username.*
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
                            Toast.makeText(activity, "Wrong username or password", Toast.LENGTH_SHORT).show()
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

    fun registerUsernameAndPasswordFromApi(activity: BaseActivity, username: String, password: String)
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
                    val tokenForRegister  = response.body()?.token

                    //val tokenResponseJsonString = Gson().toJson(tokenForRegister)
                    val editor = Constants.MSHAREDPREFERENCES.edit()
                    editor.putString(Constants.TOKEN_USER_MODEL, tokenForRegister.toString())
                    editor.apply()
                    activity.proceedToNextAct()

                   // Log.i("Response1", "$tokenResponseJsonString")

                }else{
                    val rc =  response.code()
                    when(rc){
                        400->{
                            val qwe =  response.errorBody()?.charStream()?.readText()
                            val jsonObject = JSONObject(qwe!!.trim())
                            var message = jsonObject.getJSONArray("errors")

                            for(i in 0 until message.length()){
                                Log.e("ErrorRegister", message.get(i) as String)
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
                    var ctr = 0
                    if (response.isSuccessful) {
                        val list = response.body()

                        if (list != null) {
                            list.map {
                                if (it.isCompleted == false) {
                                    ctr++
                                } else {
                                    ctr = 0
                                }
                            }
                            Log.i("counttask", ctr.toString())
                            userCallback(ctr)
                        }

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

    fun getAllUser(activity: CreateTaskActivity){
        val assignee: ArrayList<String> = ArrayList<String>()

        val loginResponseCall: Call<List<User>>? =
            ApiClass().getUserServiceHeader()?.getAllUser()

        loginResponseCall?.enqueue(object : Callback<List<User>> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        for(user in users){

                            user.username?.let { assignee.add(it) }
                            Log.i("AccountList", user.username!!)
                        }

                        val arrayAdapter = ArrayAdapter(activity, R.layout.dropdown_item_create_task, assignee)
                        activity.ac_assignee.setAdapter(arrayAdapter)
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

    fun checkTask(context: Context, id: String?){
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<Task>? =
                id?.let {
                    ApiClass().getUserServiceHeader()?.toggleTaskComplete("Bearer "+msharedToken!!,
                        it
                    )
                }

            loginResponseCall?.enqueue(object: Callback<Task> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Task>, response: Response<Task>) {

                    if(response.isSuccessful) {

                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400 showAllTask", "Bad Request")
                            }
                            403-> {
                                Log.e("Error 403", "Not Found" + rc)
                            }else ->{
                            Log.e("Error", "Happy Generic Error" + rc)
                        }
                        }
                    }
                }
                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                }
            })
        }
    }

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

                        val adapter = task?.let { TaskListItemsAdapter(activity,
                            it as MutableList<Task>
                        ) }

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

                        val adapter = task?.let { TaskListItemsAdapter(activity,
                            it as MutableList<Task>
                        ) }

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

    private var task: List<Task>? = null

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

                        task = response.body()!!
                        Log.i("MyTask1", task.toString())
                        var adapter: List<Task>
                        if (task != null) {
                            activity.rv_activity_task.layoutManager = LinearLayoutManager(activity)
                            activity.rv_activity_task.setHasFixedSize(true)

                            val adapter = TaskListItemsAdapter(activity,
                                task!! as MutableList<Task>
                            )

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

                            activity.rv_activity_task.layoutManager = LinearLayoutManager(activity)
                            activity.rv_activity_task.setHasFixedSize(true)

                            val adapter = TaskListItemsAdapter(activity,
                                task as MutableList<Task>
                            )

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

    fun editTask(activity: CreateTaskActivity, id: String?, editTask: EditTask){
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<Task>? =
                id?.let {
                    ApiClass().getUserServiceHeader()?.editTask("Bearer "+msharedToken!!, it, editTask)
                }

            loginResponseCall?.enqueue(object: Callback<Task> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if(response.isSuccessful) {
                        val task = response.body()
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                        activity.finish()

                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400 showAllTask", "Bad Request")
                            }
                            403-> {
                                Log.e("Error 403", "Not Found" + rc)
                            }else ->{
                            Log.e("Error", "Happy Generic Error" + rc)
                        }
                        }
                    }
                }
                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                }
            })
        }
    }

    fun deleteTask(context: Context, id: String?){
        if(Constants.MSHAREDPREFERENCES.contains(Constants.TOKEN_USER_MODEL)){
            Constants.MSHAREDPREFERENCES = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            val msharedToken = Constants.MSHAREDPREFERENCES.getString(Constants.TOKEN_USER_MODEL, "")

            val loginResponseCall: Call<Task>? =
                id?.let {
                    ApiClass().getUserServiceHeader()?.deleteTask("Bearer "+msharedToken!!,
                        it
                    )
                }

            loginResponseCall?.enqueue(object: Callback<Task> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if(response.isSuccessful) {
                        val task = response.body()
                    }else{
                        val rc =  response.code()
                        when(rc){
                            400->{
                                Log.e("Error 400 showAllTask", "Bad Request")
                            }
                            403-> {
                                Log.e("Error 403", "Not Found" + rc)
                            }else ->{
                                Log.e("Error", "Happy Generic Error" + rc)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("Errorrrrr", t!!.message.toString())
                }
            })
        }
    }

    fun signOut()
    {
        Constants.MSHAREDPREFERENCES.edit().clear().commit()
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val initialReq: Request = chain.request()
        val modRequest: Request = initialReq

        return chain.proceed(modRequest)
    }

}