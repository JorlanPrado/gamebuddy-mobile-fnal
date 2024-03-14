package com.mita.gamebuddymobile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mita.gamebuddymobile.api.RandomUserDataClass
import com.squareup.picasso.Picasso

class RandomUserAdapterClass(private val RandomUserList:ArrayList<RandomUserDataClass>)
    : RecyclerView.Adapter<RandomUserAdapterClass.ViewHolderClass>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.random_user_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val randomUser = RandomUserList[position]
        holder.name.text = randomUser.name
        holder.gender.text = randomUser.gender
        holder.age.text = randomUser.age.toString()

        // Load image using Picasso with user's ID as part of the URL
        val imageUrl = "https://ui-avatars.com/api/?name=${randomUser.name}&color=7F9CF5&background=random&user_id=${randomUser.id}"
        Picasso.get().load(imageUrl).into(holder.avatar)

        holder.message.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", randomUser.name)
            intent.putExtra("UserIDReceiver", randomUser.id)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return RandomUserList.size
    }
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.Random_Name)
        val gender : TextView = itemView.findViewById(R.id.Random_gender)
        val age : TextView = itemView.findViewById(R.id.Random_age)
        val message: TextView = itemView.findViewById(R.id.message)
        val avatar: ImageView = itemView.findViewById(R.id.imageView)

    }
}