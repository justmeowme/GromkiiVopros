package com.example.gromkivopros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //go to CreateRoomActivity
        val mCreateRoom = findViewById<Button>(R.id.button_createRoom);
        mCreateRoom.setOnClickListener{
            startActivity(Intent(this, CreateRoomActivity::class.java))
        }

        //go to JoinRoomActivity
        val mJoinRoom = findViewById<Button>(R.id.button_joinRoom);
        mJoinRoom.setOnClickListener{
            startActivity(Intent(this, JoinRoomActivity::class.java))
        }

        //go to GameRulesActivity
        val mRameRules = findViewById<TextView>(R.id.gameRules);
        mRameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }

    }
}