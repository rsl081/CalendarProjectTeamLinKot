package com.example.calendarprojectteamlinkot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


    }

//    private fun signInRegisteredUser()
//    {
//        val email: String = et_.text.toString().trim{ it <= ' '}
//        val password: String = et_password.text.toString().trim{ it <= ' '}
//
//        if(validateForm(email, password))
//        {
//            showProgressDialog(resources.getString(R.string.please_wait))
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    hideProgressDialog()
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d("Signin", "signInWithEmail:success")
//                        val user = auth.currentUser
//                        startActivity(Intent(this, MainActivity::class.java))
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w("Signin", "signInWithEmail:failure", task.exception)
//                        Toast.makeText(baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }else{
//
//        }
//    }

//    private fun validateForm(email: String, password: String): Boolean {
//        return when{
//            TextUtils.isEmpty(email)->{
//                showErrorSnackBar("Please enter a email")
//                false
//            }
//            TextUtils.isEmpty(password)->{
//                showErrorSnackBar("Please enter a password")
//                false
//        }else->{
//            true
//        }
//    }//end of validateForm

}