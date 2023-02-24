package com.example.gromkivopros.screens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gromkivopros.R
import com.example.gromkivopros.recycler_elements.Adapter
import com.example.gromkivopros.recycler_elements.UserModal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class WaitForStartActivity : AppCompatActivity() {

    // HERE WE ARE WAITING FOR HOST TO START A GAME
    // WE CAN SEE ALL THE USERS WHO HAVE JOINED THE GAME


    //USER ICONS
    private var usersIconsList = listOf<Int>(
        R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4,
        R.drawable.icon5, R.drawable.icon6, R.drawable.icon7, R.drawable.icon8,
        R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12,
        R.drawable.icon13, R.drawable.icon14, R.drawable.icon15, R.drawable.icon16
    )
    private var usersIconsUsedList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_for_start)

        //HIDE STATUS BAR
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //GET ROOM CODE
        val sharedPreference =  getSharedPreferences("shared", Context.MODE_PRIVATE)
        val roomCode = sharedPreference.getString("roomCode", "ERROR")


        //CREATE RECYCLERVIEW
        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_allPlayers)
        val layoutManager = GridLayoutManager(this, 3)
        val usersList = ArrayList<UserModal>()
        val mAdapter = Adapter(usersList, this)
        val nickList = mutableListOf<String>()
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.hasFixedSize()
        mRecyclerView.adapter = mAdapter


        //CONNECT TO FIREBASE AND WAIT FOR GAME TO START
        val database = Firebase.database
        val intent = Intent(this, StartAnimationActivity::class.java)

        database.reference.child("rooms").child(roomCode.toString()).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.child("users").children
                val isStarted = snapshot.child("started").value.toString()

                if (isStarted=="1"){
                    startActivity(intent)
                }

                //ADD NEW USERS TO RECYCLER
                data.forEach{
                    val key = it.value.toString()
                    if (!(nickList.contains(key) || key == "0")){
                        nickList.add(key)
                        usersList.add(UserModal(key, choseIcon()))
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "cannot connect to database")
            }
        })


        //UI
        val mTextView = findViewById<TextView>(R.id.text_roomCode);
        mTextView.text = roomCode


        //GO TO GameRulesActivity.kt
        val mGameRules = findViewById<TextView>(R.id.text_gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }

    }

    //RANDOM INT
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