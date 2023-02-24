package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.gromkivopros.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameActivity : AppCompatActivity() {

    //IT IS MAIN GAME ACTIVITY
    //QUESTION SHOWS UP  AT HOST SCREEN AND IS HIDDEN ON OTHER SCREENS
    //MUSIC PLAYS FOR 1m 3s TILL THE END OF TIMER
    //WHEN TIMER ENDS USERS GO TO CheckAnswerActivity.kt

    private val database = Firebase.database

    private var mMediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //GET HOST AND ROOM CODE
        val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
        val host = sharedPreference.getString("isHost", "no").toString()
        val nickname = sharedPreference.getString("nickname", "user").toString()
        val roomCode = sharedPreference.getString("roomCode", "ERROR")

        //UI ELEMENTS
        val mTimer = findViewById<TextView>(R.id.timer)
        val mQuestionNumber = findViewById<TextView>(R.id.textView_questionNumber)
        val mQuestion = findViewById<TextView>(R.id.question)
        val mAnswer = findViewById<EditText>(R.id.input_userAnswer)
        val mSubmitAnswer = findViewById<Button>(R.id.button_submitAnswer)
        val mWait = findViewById<TextView>(R.id.wait_answer)
        val mTextInfo = findViewById<TextView>(R.id.textView6)
        val mProgress = findViewById<ProgressBar>(R.id.progressBar)

        mTimer.visibility = View.VISIBLE
        mQuestionNumber.visibility = View.VISIBLE
        mAnswer.visibility = View.VISIBLE
        mSubmitAnswer.visibility = View.VISIBLE
        mTextInfo.visibility = View.VISIBLE


        //SUBMIT ANSWER
        mSubmitAnswer.setOnClickListener{
            val answer = mAnswer.text.toString()
            database.getReference("rooms").child(roomCode.toString()).child("answers").updateChildren(mapOf(nickname to answer))
            mAnswer.visibility = View.INVISIBLE
            mSubmitAnswer.visibility = View.INVISIBLE
            mTextInfo.visibility = View.INVISIBLE
            mWait.visibility = View.VISIBLE
            mProgress.visibility = View.VISIBLE
        }


        //CHECK IF HOST
        if (host=="no"){
            mQuestion.visibility = View.INVISIBLE
        } else{
            mQuestion.visibility = View.VISIBLE
        }


        //INITIALISE INTENT
        val intent = Intent(this, CheckAnswerActivity::class.java)


        //PUT QUESTION
        database.reference.child("rooms").child(roomCode.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = snapshot.child("questions").children
                val questionNumber = snapshot.child("current_question").value.toString()

                var count = 1
                questions.forEach{
                    if (it.key.toString()!="0" && it.key.toString()!="arity"){
                        if (count.toString()==questionNumber){
                            val question = it.key.toString()
                            mQuestion.text = question
                            mQuestionNumber.text = "$questionNumber/7"
                        }
                        count += 1
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "cannot connect to database")
            }
        })


        //GAME TIMER
        object : CountDownTimer(77000, 1000) {
            override fun onTick(m: Long) {
                if ((m / 1000).toInt() >= 15){

                    if ((m / 1000 - 15).toInt() >= 60) {
                        mTimer.text = "01:00"
                    } else {
                        if ((m / 1000 - 15).toInt() < 10) {
                            mTimer.text = "00:0${(m / 1000 - 15)}"
                        } else {
                            if ((m / 1000-15).toInt() < 0){
                                mTimer.text = "00:00"
                            } else{
                                mTimer.text = "00:${(m / 1000 - 15)}"
                            }
                        }
                    }
                } else{
                    mQuestion.text = "У вас есть 15 секунд, чтобы написать ответ. Обсуждать его уже нельзя"
                    mQuestion.visibility = View.VISIBLE

                    if ((m / 1000).toString() == "60") {
                        mTimer.text = "01:00"
                    } else {
                        if ((m / 1000).toInt() < 9) {
                            mTimer.text = "00:0${(m / 1000)+1}"
                        } else {
                            mTimer.text = "00:${(m / 1000)+1}"
                        }
                    }
                }
            }

            override fun onFinish() {
                startActivity(intent)
            }
        }.start()


    }

}
