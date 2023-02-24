package com.example.gromkivopros.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.gromkivopros.R

class SplashActivity : AppCompatActivity() {

    //IT'S JUST A SPLASH WITH ANIMATION
    //IT INSTANTLY REDIRECT US TO MainActivity.kt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //LOGO ANIMATION
        val mLogo = findViewById<ImageView>(R.id.logo);
        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        mLogo.startAnimation(animation)

        //GO TO MainActivity.kt
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 4000)



    }
}