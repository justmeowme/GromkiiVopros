package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gromkivopros.R
import com.example.gromkivopros.recycler_elements_answers.AdapterAnswer
import com.example.gromkivopros.recycler_elements_answers.UserModalAnswer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


//HERE WE CAN CHECK ALL THE USER ANSWERS BY CLICKING ON THEM
//IMPORTANT! NEED TO ADD A FEATURE WHERE ONLY USER ITSELF CAN OPEN HIS/HER ANSWER
class CheckAnswerActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_check_answer)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //RECYCLER VIEW SETTINGS
        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_allAnswers)
        val usersList = ArrayList<UserModalAnswer>()
        val nicknamesList = mutableListOf<String>()
        val mAdapter = AdapterAnswer(usersList, this)
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.hasFixedSize()
        mRecyclerView.adapter = mAdapter


        //GET ROOM CODE
        val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
        val roomCode = sharedPreference.getString("roomCode", "ERROR")


        //CONNECT TO DATABASE TO RECEIVE INFO ABOUT NEW USERS
        val database = Firebase.database
        database.reference.child("rooms").child(roomCode.toString()).child("answers").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val key = it.key.toString()
                    if (!(nicknamesList.contains(key) || key == "0" || it.value.toString()=="0")){
                        nicknamesList.add(key)
                        usersList.add(UserModalAnswer(key, choseIcon(), it.value.toString()))
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "error in users receiver")
            }
        })

        //BUTTON NEXT
        val mButton = findViewById<Button>(R.id.checkAnswer)
        mButton.setOnClickListener{
            startActivity(Intent(this, CorrectAnswerActivity::class.java))
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