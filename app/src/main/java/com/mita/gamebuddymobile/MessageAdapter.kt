package com.mita.gamebuddymobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mita.gamebuddymobile.api.Message

class MessageAdapter(val context: Context, private val messageList: ArrayList<Message>, private val userId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_RECEIVE = 1
        const val ITEM_SENT = 2
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_RECEIVE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver_message, parent, false)
            ReceiveViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sender_message, parent, false)
            SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (currentMessage.senderId.toString() == userId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messageList.isEmpty()) return

        val currentMessage = messageList[position]

        if (holder is SentViewHolder) {
            holder.sendMessage.text = currentMessage.body
        } else if (holder is ReceiveViewHolder) {
            holder.receiveMessage.text = currentMessage.body
        }
    }
}
