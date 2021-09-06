package com.example.calendarprojectteamlinkot.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity()
{
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                finish()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onStart() {
        super.onStart()
        iv_logo.startAnimation(animation)
        tv_app_name.startAnimation(animation)
        tv_app.startAnimation(animation)
    }
}