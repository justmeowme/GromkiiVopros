package com.example.gromkivopros.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gromkivopros.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class JoinRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //go to GameRulesActivity
        val mGameRules = findViewById<TextView>(R.id.text_gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }

        //join room
        val mRoom_code = findViewById<EditText>(R.id.input_roomCode)
        val mJoin_room = findViewById<Button>(R.id.button_startGame)
        val mUser_nickname = findViewById<EditText>(R.id.input_userName)

        fun getRandomString(length: Int) : String {
            val allowedChars = ('A'..'Z') + ('0'..'9')
            return (1..length).map { allowedChars.random() }.joinToString("")
        }

        //we need add new member to room only once
        var added_once = 0;


        mJoin_room.setOnClickListener{
            //getting nickname and room code
            val user_nickname = mUser_nickname.text.toString().trim()
            val room_code = mRoom_code.text.toString().trim()

            //putting code to next activity
            val intent = Intent(this, WaitForStartActivity::class.java)
            intent.putExtra("room_code", room_code)

            //connecting to database
            val database = Firebase.database
            database.reference.child("rooms").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var data = snapshot.children
                    //looking for the correct key
                    data.forEach{
                        var key = it.key.toString()

                        //adding member to room
                        if (key==room_code && added_once==0){
                            added_once = 1
                            database.getReference("rooms").child(room_code).child("users").updateChildren(mapOf("user"+getRandomString(8) to user_nickname)).addOnSuccessListener {
                                startActivity(intent)
                            }.addOnFailureListener{
                                Log.d("error", "error")
                            }
                        } else{
                            Log.d("status", "incorrect")
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }

}