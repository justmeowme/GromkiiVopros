package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import com.example.gromkivopros.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StartAnimationActivity : AppCompatActivity() {

    //HERE ARE ANIMATIONS FOR START ACTIVITY

    private val database = Firebase.database

    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_animation)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // START MUSIC
        playSound()

        //GET HOST AND ROOM CODE
        val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
        val host = sharedPreference.getString("isHost", "no")
        val roomCode = sharedPreference.getString("roomCode", "ERROR")

        //UI ELEMENTS
        val mText = findViewById<TextView>(R.id.sound_textView);
        val mProgress = findViewById<ProgressBar>(R.id.progressBar)

        mText.visibility = View.VISIBLE
        mProgress.visibility = View.INVISIBLE

        //INITIALISE INTENT
        val intent = Intent(this, GameActivity::class.java)

        //INITIAL ANIMATIONS
        database.getReference("rooms").child(roomCode.toString()).child("started").setValue("playing")
        mProgress.visibility = View.INVISIBLE
        mText.visibility = View.VISIBLE
        digitsAnimations(mText, intent)

    }

    //SOUND FUNCTIONS
    private fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.sound)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }


    // DIGITS ANIMATIONS
    // P.S I KNOW THAT HANDLER INSIDE ANOTHER HANDLER IS A BAD WAY TO DO AN ANIMATION, BUT HAVE NO IDEA HOW TO DO IT IN OTHER WAY
    fun digitsAnimations(mText: TextView, intent: Intent){
        mText.visibility = View.VISIBLE

        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        mText.startAnimation(animation)
        var flag = 1

        Handler(Looper.getMainLooper()).postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                Handler(Looper.getMainLooper()).postDelayed({
                    Handler(Looper.getMainLooper()).postDelayed({
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (flag==1){
                                startActivity(intent)
                                flag = 0;
                            }
                        }, 3000)
                        mText.text = "Начали!"
                        mText.textSize = 40F
                        val animationDigit = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
                        mText.startAnimation(animationDigit)

                    }, 1000)

                    mText.text = "1"
                    mText.textSize = 300F
                    val animationDigit = AnimationUtils.loadAnimation(this, R.anim.zoom_out_for_digit)
                    mText.startAnimation(animationDigit)

                }, 1000)

                mText.text = "2"
                mText.textSize = 300F
                val animationDigit = AnimationUtils.loadAnimation(this, R.anim.zoom_out_for_digit)
                mText.startAnimation(animationDigit)

            }, 1000)

            mText.text = "3"
            mText.textSize = 300F
            val animationDigit = AnimationUtils.loadAnimation(this, R.anim.zoom_out_for_digit)
            mText.startAnimation(animationDigit)

        }, 4000)
    }
}