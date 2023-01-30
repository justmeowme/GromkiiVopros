package com.example.gromkivopros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class CreateRoomActivity : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: Adapter
    lateinit var usersList: ArrayList<UserModal>

    var usersIconsList = listOf<Int>(R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7, R.drawable.icon8, R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12, R.drawable.icon13, R.drawable.icon14, R.drawable.icon15, R.drawable.icon16)
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

        //go to GameRulesActivity
        val mGameRules = findViewById<TextView>(R.id.text_gameRules);
        mGameRules.setOnClickListener{
            startActivity(Intent(this, GameRulesActivity::class.java))
        }

        //recyclerview with GridLayoutManager
        mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_allPlayers)
        usersList = ArrayList()
        val layoutManager = GridLayoutManager(this, 3)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.hasFixedSize()
        mAdapter = Adapter(usersList, this)
        mRecyclerView.adapter = mAdapter


        //add items to usersList
        usersList.add(UserModal("savvashu", choseIcon()))
        usersList.add(UserModal("meowme", choseIcon()))
        usersList.add(UserModal("wishnya", choseIcon()))
        usersList.add(UserModal("dizanio", choseIcon()))
        usersList.add(UserModal("gurovr", choseIcon()))


        //saying that we changed data
        mAdapter.notifyDataSetChanged()

    }
}