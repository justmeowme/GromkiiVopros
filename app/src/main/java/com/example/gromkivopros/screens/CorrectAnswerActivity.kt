package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.gromkivopros.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//ACTIVITY WHERE USERS CHECK IF THEIR GROUP ANSWER IS CORRECT
//IF ONE OR MORE ANSWERS IN GROUP ARE INCORRECT, THEN IT DOESN'T COUNT

class CorrectAnswerActivity : AppCompatActivity() {

    var correctAnswersAmount: String = ""
    var questionNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correct_answer)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //GET HOST AND ROOM CODE
        val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
        val roomCode = sharedPreference.getString("roomCode", "ERROR")
        val host = sharedPreference.getString("isHost", "no")

        val arrayAnswers = mutableListOf<String>()
        val arrayCorrectAnswers = mutableListOf<String>()
        val intent = Intent(this, StartAnimationActivity::class.java)


        //GET ANSWERS
        val database = Firebase.database
        database.reference.child("rooms").child(roomCode.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = snapshot.child("questions").children
                val answers = snapshot.child("answers").children
                correctAnswersAmount = snapshot.child("correct_answers").value.toString()
                questionNumber = snapshot.child("current_question").value.toString()

                //GET ALL ANSWERS
                answers.forEach{
                    if (it.key.toString()!="0" && it.key.toString()!="arity"){
                        arrayAnswers.add(it.value.toString())
                        Log.d("ans0", it.value.toString())
                    }
                }

                //GET CORRECT ANSWERS
                var count = 1
                questions.forEach{
                    if (it.key.toString()!="0" && it.key.toString()!="arity"){
                        if (count.toString()==questionNumber){
                            val lst = it.value as MutableList<String>
                            lst.forEach{
                                arrayCorrectAnswers.add(it)
                            }
                        }
                        count += 1
                    }
                }

                //UI
                val mQuestionNumber = findViewById<TextView>(R.id.textView_questionNumber3)
                mQuestionNumber.text = "$questionNumber/7"
                val mText = findViewById<TextView>(R.id.correctCount)
                mText.text = "$correctAnswersAmount / $questionNumber"
                val mAnswer = findViewById<TextView>(R.id.answer)
                val mTextAbout = findViewById<TextView>(R.id.textView5)
                val mNextQuestion = findViewById<Button>(R.id.nextQuestion)

                if (host=="yes"){
                    mNextQuestion.visibility = View.VISIBLE
                    mTextAbout.visibility = View.INVISIBLE
                } else{
                    mNextQuestion.visibility = View.INVISIBLE
                    mTextAbout.visibility = View.VISIBLE
                }


                //CHECK CORRECT
                var correct = 1
                arrayAnswers.forEach{
                    Log.d("ans", it)
                    if (!arrayCorrectAnswers.contains(it)){
                        correct = 0
                    }
                }

                //CHECK ANSWER
                mAnswer.setOnClickListener{
                    mAnswer.text = arrayCorrectAnswers[0]
                    mAnswer.setTextColor(Color.parseColor("#FFFFFF"))
                    if (correct==1){
                        mAnswer.setBackgroundResource(R.drawable.correct)
                        correctAnswersAmount = (correctAnswersAmount.toInt() + 1).toString()
                        mText.text = "$correctAnswersAmount / $questionNumber"
                    } else{
                        mAnswer.setBackgroundResource(R.drawable.incorrect)
                    }
                }

                //NEXT QUESTION
                mNextQuestion.setOnClickListener{
                    database.getReference("rooms").child(roomCode.toString()).child("started").setValue("1")
                    database.getReference("rooms").child(roomCode.toString()).child("current_question").setValue(questionNumber.toInt()+1)
                    database.getReference("rooms").child(roomCode.toString()).child("correct_answers").setValue(correctAnswersAmount.toInt())
                    database.getReference("rooms").child(roomCode.toString()).child("answers").setValue({})
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "cannot connect to database")
            }
        })


    }
}