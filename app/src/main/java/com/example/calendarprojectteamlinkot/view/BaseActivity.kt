package com.example.calendarprojectteamlinkot.view

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.calendarprojectteamlinkot.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_progress.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgressDialog(text: String)
    {
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text  = text

        mProgressDialog.show()
    }

    fun hideProgressDialog()
    {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();

        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        },2000)
    }

    fun showErrorSnackBar(message: String)
    {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error_color))
        snackBar.show()

    }

    fun toggleDrawer(){
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    fun setupActionBar(context: AppCompatActivity, toolbar: Toolbar){
        context.setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    fun displayCurrentDate(): String {
        val sdf = SimpleDateFormat("EEE, MMM d, yyyy")
        val calendar = Calendar.getInstance()
        val currentDate: String = sdf.format(calendar.time)

        return currentDate
    }

    fun showDate(year: Int, month: Int, day: Int): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        val currentDate: String = sdf.format(calendar.time)

        return currentDate
    }

    fun dayOfMonth(year: Int, month: Int, day: Int): String {
        val sdf = SimpleDateFormat("EEE, MMM d, yyyy")
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        val currentDate: String = sdf.format(calendar.time)

        return currentDate
    }




}