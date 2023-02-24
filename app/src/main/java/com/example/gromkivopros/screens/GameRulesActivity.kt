package com.example.gromkivopros.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.gromkivopros.R

class GameRulesActivity : AppCompatActivity() {

    //JUST AN ACTIVITY WITH GAME RULES
    //WITH ABILITY TO GO TO PREVIOUS SCREEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_rules)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //GO TO PREVIOUS ACTIVITY
        val mButtonBack = findViewById<Button>(R.id.button_goBack)
        mButtonBack.setOnClickListener{
            this.finish()
        }
    }
}