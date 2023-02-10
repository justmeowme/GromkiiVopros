package com.example.gromkivopros.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: Adapter
    lateinit var usersList: ArrayList<UserModal>
    lateinit var dbRef: DatabaseReference

    var usersIconsList = listOf<Int>(
        R.drawable.icon1,
        R.drawable.icon2,
        R.drawable.icon3,
        R.drawable.icon4,
        R.drawable.icon5,
        R.drawable.icon6,
        R.drawable.icon7,
        R.drawable.icon8,
        R.drawable.icon9,
        R.drawable.icon10,
        R.drawable.icon11,
        R.drawable.icon12,
        R.drawable.icon13,
        R.drawable.icon14,
        R.drawable.icon15,
        R.drawable.icon16
    )
    var usersIconsUsedList = mutableListOf<Int>()

    fun rand(from: Int, to: Int) : Int {
        val random = Random()
        return random.nextInt(to - from) + from
    }

    fun <T> Collection<T>.filterNotIn(collection: Collection<T>): Collection<T> {
        val set = collection.toSet()
        return filterNot { set.contains(it) }
    }

    //chose random icon
    fun choseIcon(): Int{
        var neededList = mutableListOf<Int>()
        neededList = usersIconsList.filterNotIn(usersIconsUsedList) as MutableList<Int>
        var a = rand(0, neededList.size)
        usersIconsUsedList.add(neededList[a])
        return neededList[a]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //Generate game code
        val mTextView = findViewById<TextView>(R.id.text_roomCode);

        val intent = getIntent();
        var room_code = intent.getStringExtra("room_code")
        mTextView.text = room_code

        var roomHost = findViewById<EditText>(R.id.input_userName)

        //go to GameRulesActivity
        val mGameRules = findViewById<TextView>(R.id.text_gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }

        usersList = ArrayList()
        var nickList = mutableListOf<String>()

        //recyclerview with GridLayoutManager
        mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_allPlayers)
        val layoutManager = GridLayoutManager(this, 3)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.hasFixedSize()
        mAdapter = Adapter(usersList, this)
        mRecyclerView.adapter = mAdapter

        val database = Firebase.database
        database.reference.child("rooms").child(room_code.toString()).child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.children
                data.forEach{
                    var key = it.value.toString()
                    if (nickList.contains(key) || key == "0"){
                        //do nothing
                    } else{
                        nickList.add(key)
                        usersList.add(UserModal(key, choseIcon()))
                        mAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}