package com.example.calendarprojectteamlinkot.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.calendarprojectteamlinkot.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar(this, toolbar_main_activity)

        nav_view.setNavigationItemSelectedListener(this)
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
                Toast.makeText(this@MainActivity,
                    "Calendar View",
                    Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my_tasks -> {
                Toast.makeText(this@MainActivity,
                    "Tasks View",
                    Toast.LENGTH_SHORT).show()
            }
            R.id.nav_sign_out ->{
                Toast.makeText(this@MainActivity,
                    "Sign Out",
                    Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

}
