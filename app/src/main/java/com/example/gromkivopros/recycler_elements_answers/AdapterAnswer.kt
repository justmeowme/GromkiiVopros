package com.example.gromkivopros.recycler_elements_answers

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gromkivopros.R

class AdapterAnswer(private val itemsList: ArrayList<UserModalAnswer>, private val context: Context) : RecyclerView.Adapter<AdapterAnswer.ItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.answer_recyclerview_item, parent, false)
        return ItemsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.userNickname.text = itemsList.get(position).userNickname
        holder.userPicture.setImageResource(itemsList.get(position).userPicture)

        holder.userAnswer.setOnClickListener{
            holder.userAnswer.text = itemsList.get(position).userAnswer
            holder.userAnswer.setBackgroundResource(R.drawable.button_transparent)
            holder.userAnswer.setTextColor(Color.parseColor("#B5FF00"))
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNickname: TextView = itemView.findViewById(R.id.textView_userNickname)
        val userPicture: ImageView = itemView.findViewById(R.id.imageView_userPicture)
        val userAnswer: TextView = itemView.findViewById(R.id.text)
    }
}