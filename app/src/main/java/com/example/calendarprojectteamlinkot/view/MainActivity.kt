package com.example.calendarprojectteamlinkot.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()



    }

    private fun init(){

        Constants.MSHAREDPREFERENCES = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        setupActionBar(this, toolbar_main_activity)

        tv_name_activtyday.text = "Hi! " +ApiClass().getCurrentUser()

        nav_view.setNavigationItemSelectedListener(this)

        nav_sign_out.setOnClickListener {
            ApiClass().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }




    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_calendar ->{
                activity_task.visibility = View.INVISIBLE
                activity_day.visibility = View.VISIBLE
            }
            R.id.nav_my_tasks -> {
                activity_day.visibility = View.INVISIBLE
               activity_task.visibility = View.VISIBLE
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }


}
