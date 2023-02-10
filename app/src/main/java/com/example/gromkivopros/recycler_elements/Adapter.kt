package com.example.gromkivopros.recycler_elements

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gromkivopros.R

class Adapter(private val itemsList: ArrayList<UserModal>, private val context: Context) : RecyclerView.Adapter<Adapter.ItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_recyclerview_item, parent, false)
        return ItemsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.userNickname.text = itemsList.get(position).userNickname
        holder.userPicture.setImageResource(itemsList.get(position).userPicture)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNickname: TextView = itemView.findViewById(R.id.textView_userNickname)
        val userPicture: ImageView = itemView.findViewById(R.id.imageView_userPicture)
    }
}