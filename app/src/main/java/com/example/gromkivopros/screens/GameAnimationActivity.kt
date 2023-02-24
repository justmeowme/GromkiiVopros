package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.gromkivopros.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameAnimationActivity : AppCompatActivity() {

    //HERE ARE ANIMATIONS FOR START ACTIVITY

    private val database = Firebase.database

    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_animation)

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

        //INITIALISE INTENT
        val intent = Intent(this, GameActivity::class.java)

        //INITIAL ANIMATIONS
        database.reference.child("rooms").child(roomCode.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.child("started").value.toString()
                if (data=="1"){
                    database.getReference("rooms").child(roomCode.toString()).child("started").setValue("playing")
                    mProgress.visibility = View.INVISIBLE
                    digitsAnimations(mText, intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "cannot connect to database")
            }
        })

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
        mText.isVisible = true
        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        mText.startAnimation(animation)
        var flag = 1

        Handler(Looper.getMainLooper()).postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                Handler(Looper.getMainLooper()).postDelayed({
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (flag==1){
                            startActivity(intent)
                            flag = 0;
                        }


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

        }, 3500)
    }
}