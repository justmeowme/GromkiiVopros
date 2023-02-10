package com.example.gromkivopros.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.gromkivopros.R

class GameRulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_rules)

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //go to previous activity
        val mButtonBack = findViewById<Button>(R.id.button_goBack)
        mButtonBack.setOnClickListener{
            this.finish()
        }
    }
}