package com.example.gromkivopros.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.gromkivopros.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun rand(from: Int, to: Int) : Int {
            val random = Random()
            return random.nextInt(to - from) + from
        }

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //go to CreateRoomActivity
        val mCreateRoom = findViewById<Button>(R.id.button_createRoom);
        mCreateRoom.setOnClickListener{

            fun getRandomString(length: Int) : String {
                val allowedChars = ('A'..'Z') + ('0'..'9')
                return (1..length).map { allowedChars.random() }.joinToString("")
            }

            var room_code = getRandomString(5)

            //val roomAPI = RetrofitProvider.getInstance().create(RoomInterface::class.java)
            //GlobalScope.launch {
            //    val result = roomAPI.createRoom()
            //    if (result != null){
            //        var room = result.body()
            //    }
            //}

            val database = Firebase.database.getReference("rooms")

            database.child(room_code).setValue(mapOf("room_code" to room_code, "users" to {})).addOnSuccessListener {
                Toast.makeText(this, "Data added", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }


            var intent = Intent(this, CreateRoomActivity::class.java)
            intent.putExtra("room_code", room_code)
            startActivity(intent)
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