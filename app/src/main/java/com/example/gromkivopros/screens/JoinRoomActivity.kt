package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.gromkivopros.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class JoinRoomActivity : AppCompatActivity() {

    //HERE WE CAN JOIN ROOM BY ENTERING CODE
    //AFTER JOINING THE ROOM WE WILL SEE ALL THE USERS WHO HAVE ALREADY JOINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //UI
        val mRoomCode = findViewById<EditText>(R.id.input_roomCode)
        val mUserNickname = findViewById<EditText>(R.id.input_userName)


        //GO TO JoinRoomActivity.kt
        val mJoinRoom = findViewById<Button>(R.id.button_startGame)
        var addedOnce = 0;

        mJoinRoom.setOnClickListener{
            val userNickname = mUserNickname.text.toString().trim()
            val roomCode = mRoomCode.text.toString().trim()
            val intent = Intent(this, WaitForStartActivity::class.java)

            //CONNECT TO FIREBASE
            val database = Firebase.database
            database.reference.child("rooms").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val key = it.key.toString()

                        //ADD MEMBER TO ROOM
                        if (key==roomCode && addedOnce==0){
                            addedOnce = 1
                            database.getReference("rooms").child(roomCode).child("users").updateChildren(mapOf("user"+getRandomString(8) to userNickname)).addOnSuccessListener {
                                val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
                                val editor = sharedPreference.edit()
                                editor.putString("roomCode", roomCode)
                                editor.putString("isHost", "no")
                                editor.putString("nickname", userNickname)
                                editor.apply()
                                startActivity(intent)
                            }.addOnFailureListener{
                                Log.d("error", "cannot add a user")
                            }
                        } else{
                            Log.d("status", "room code is incorrect")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("error", "cannot connect to database")
                }
            })
        }


        //GO TO GameRulesActivity.kt
        val mGameRules = findViewById<TextView>(R.id.text_gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }
    }

    //GET RANDOM STRING
    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }

}