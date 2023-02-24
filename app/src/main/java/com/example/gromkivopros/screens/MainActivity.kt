package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.gromkivopros.R

class MainActivity : AppCompatActivity() {

    //HERE WE CAN CHOSE IF WE WANT TO CREATE ROOM OR TO JOIN ROOM
    //IF WE CHOSE TO CREATE ROOM THAN HERE WE ALSO GENERATE ROOM CODE AND SAVE IT
    //ALSO HERE WE CAN GO TO GAME RULES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //GO TO CreateRoomActivity.kt
        val mCreateRoom = findViewById<Button>(R.id.button_createRoom);
        mCreateRoom.setOnClickListener{
            val roomCode = getRandomString()

            //CREATE SHARED PREFERENCES
            val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("roomCode", roomCode)
            editor.apply()

            startActivity(Intent(this, CreateRoomActivity::class.java))
        }

        //GO TO JoinRoomActivity.kt
        val mJoinRoom = findViewById<Button>(R.id.button_joinRoom);
        mJoinRoom.setOnClickListener{
            startActivity(Intent(this, JoinRoomActivity::class.java))
        }

        //GO TO GameRulesActivity.kt
        val mGameRules = findViewById<TextView>(R.id.gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }

    }

    private fun getRandomString() : String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..5).map { allowedChars.random() }.joinToString("")
    }
}