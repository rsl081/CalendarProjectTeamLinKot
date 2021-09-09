package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.ChangePassword
import com.example.calendarprojectteamlinkot.repository.ApiClass
import kotlinx.android.synthetic.main.activity_change_pass.*

class ChangePassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)


        btn_changePass.setOnClickListener {
            val oldPassword = old_password_change_activity.text.toString().trim{ it <= ' '}
            val newPassword = new_password_channge_passsword.text.toString().trim{ it <= ' '}
            if(validateForm(oldPassword, newPassword)){
                val password = ChangePassword(oldPassword, newPassword)
                ApiClass().changePasswordUser(this,password)
            }
        }
    }

    private fun validateForm(oldpass: String, newpass: String): Boolean {
        return when{
            TextUtils.isEmpty(oldpass)->{
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
                false
            }
            TextUtils.isEmpty(newpass)->{
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
                false
            }else->{
                true
            }
        }
    }//end of validateForm

    fun proceedToNextAct(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}