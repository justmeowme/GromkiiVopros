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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gromkivopros.recycler_elements.Adapter
import com.example.gromkivopros.R
import com.example.gromkivopros.recycler_elements.UserModal
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class CreateRoomActivity : AppCompatActivity() {

    //HERE WE CREATE NEW ROOM OF USERS AND ADD IT TO DATABASE
    //ALSO WE CAN SEE ALL THE USERS WHO ARE CONNECTED TO GAME
    //GAME CAN BE STARTED BY CLICKING START BUTTON ON THIS SCREEN

    //ICONS
    private var usersIconsList = listOf(
        R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4,
        R.drawable.icon5, R.drawable.icon6, R.drawable.icon7, R.drawable.icon8,
        R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12,
        R.drawable.icon13, R.drawable.icon14, R.drawable.icon15, R.drawable.icon16
    )
    private var usersIconsUsedList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)


        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //GET ROOM CODE
        val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
        val roomCode = sharedPreference.getString("roomCode", "ERROR")


        //RECYCLER VIEW SETTINGS
        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_allPlayers)
        val usersList = ArrayList<UserModal>()
        val nicknamesList = mutableListOf<String>()
        val layoutManager = GridLayoutManager(this, 3)

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.hasFixedSize()
        val mAdapter = Adapter(usersList, this)
        mRecyclerView.adapter = mAdapter


        //CREATE ROOM
        val database = Firebase.database
        if (roomCode != null) {
            database.getReference("rooms").child(roomCode.toString()).setValue(mapOf("room_code" to roomCode, "host" to "", "users" to {}, "started" to 0, "current_question" to 1, "correct_answers" to 0, "answers" to {}, "questions" to {}))
        }


        //GET QUESTIONS
        val databaseQuestions = Firebase.database

        databaseQuestions.reference.child("questions").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.children.shuffled()
                var i = 0;

                data.forEach{
                    if (i < 7){
                        database.getReference("rooms").child(roomCode.toString()).child("questions").updateChildren(mapOf(it.key to it.value))
                        i += 1
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "error in questions receiver")
            }

        })


        //CONNECT TO DATABASE TO RECEIVE INFO ABOUT NEW USERS
        database.reference.child("rooms").child(roomCode.toString()).child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val key = it.value.toString()
                    if (!(nicknamesList.contains(key) || key == "0")){
                        nicknamesList.add(key)
                        usersList.add(UserModal(key, choseIcon()))
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "error in users receiver")
            }
        })



        //UI
        val mTextView = findViewById<TextView>(R.id.text_roomCode);
        var roomHost = findViewById<EditText>(R.id.input_userName)
        mTextView.text = roomCode


        //GO TO GameRulesActivity.kt
        val mGameRules = findViewById<TextView>(R.id.text_gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }


        //GO TO GameActivity.kt
        val mGame = findViewById<Button>(R.id.button_startGame)
        mGame.setOnClickListener{
            Log.d("room_host", roomHost.text.toString())
            val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("isHost", "yes")
            editor.putString("nickname", roomHost.text.toString())
            editor.apply()
            database.reference.child("rooms").child(roomCode.toString()).child("started").setValue("1")
            database.reference.child("rooms").child(roomCode.toString()).child("host").setValue(roomHost.text.toString())
            startActivity(Intent(this, StartAnimationActivity::class.java))
        }

    }

    //GET RANDOM INT
    private fun rand(to: Int) : Int {
        val random = Random()
        return random.nextInt(to)
    }

    //GET ITEM FROM COLLECTION
    private fun <T> Collection<T>.filterNotIn(collection: Collection<T>): Collection<T> {
        val set = collection.toSet()
        return filterNot { set.contains(it) }
    }

    //CHOSE RANDOM ICON
    fun choseIcon(): Int{
        val neededList = usersIconsList.filterNotIn(usersIconsUsedList) as MutableList<Int>
        val a = rand(neededList.size)
        usersIconsUsedList.add(neededList[a])
        return neededList[a]
    }
}