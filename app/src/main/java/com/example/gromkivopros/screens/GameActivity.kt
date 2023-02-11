package com.example.gromkivopros.screens

import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.gromkivopros.R

class GameActivity : AppCompatActivity() {

    var mMediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        playSound()

        //initial text animation
        val mText = findViewById<TextView>(R.id.sound_textView);
        mText.isVisible = true
        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        mText.startAnimation(animation)

        //initialize values
        val mQuestionNumber = findViewById<TextView>(R.id.textView_questionNumber)
        val mTimer = findViewById<TextView>(R.id.timer)
        val mQuestion = findViewById<TextView>(R.id.question)
        val mAns = findViewById<TextView>(R.id.textView6)
        val mAnswer = findViewById<TextView>(R.id.input_userAnswer)
        val mSubmitAnswer = findViewById<Button>(R.id.button_submitAnswer)

        //digits animation
        Handler(Looper.getMainLooper()).postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                Handler(Looper.getMainLooper()).postDelayed({
                    Handler(Looper.getMainLooper()).postDelayed({

                        //make game items visible
                        mText.textSize = 0F
                        mText.visibility = View.GONE
                        mQuestionNumber.visibility = View.VISIBLE
                        mTimer.visibility = View.VISIBLE
                        mQuestion.visibility = View.VISIBLE
                        mAns.visibility = View.VISIBLE
                        mAnswer.visibility = View.VISIBLE
                        mSubmitAnswer.visibility = View.VISIBLE

                        //setting timer
                        object : CountDownTimer(60000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                if ((millisUntilFinished/1000).toString()=="60"){
                                    mTimer.setText("01:00")
                                } else{
                                    if ((millisUntilFinished/1000).toString().toInt() < 10){
                                        mTimer.setText("00:0"+(millisUntilFinished / 1000))
                                    } else{
                                        mTimer.setText("00:"+(millisUntilFinished / 1000))
                                    }
                                }
                            }
                            override fun onFinish() {
                                //setting second timer
                                stopSound()
                                mQuestion.setText("У вас есть 15 секунд, чтобы написать ответ. Обсуждать уже нельзя!")
                                object : CountDownTimer(15000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        if ((millisUntilFinished/1000).toInt() < 10){
                                            mTimer.setText("00:0"+(millisUntilFinished / 1000))
                                        } else{
                                            mTimer.setText("00:"+(millisUntilFinished / 1000))
                                        }
                                    }
                                    override fun onFinish() {
                                        //go to check answer screen
                                    }
                                }.start()
                            }
                        }.start()


                    }, 1000)
                    mText.text = "1"
                    mText.textSize = 300F
                    val animation_digit = AnimationUtils.loadAnimation(this, R.anim.zoom_out_for_digit)
                    mText.startAnimation(animation_digit)
                }, 1000)
                mText.text = "2"
                mText.textSize = 300F
                val animation_digit = AnimationUtils.loadAnimation(this, R.anim.zoom_out_for_digit)
                mText.startAnimation(animation_digit)
            }, 1000)
            mText.text = "3"
            mText.textSize = 300F
            val animation_digit = AnimationUtils.loadAnimation(this, R.anim.zoom_out_for_digit)
            mText.startAnimation(animation_digit)
        }, 3500)



    }

    fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.sound)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }
}