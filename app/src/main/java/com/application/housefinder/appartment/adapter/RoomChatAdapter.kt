package com.application.housefinder.appartment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.housefinder.appartment.Application
import com.application.housefinder.appartment.databinding.ItemRoomChatBinding
import com.application.housefinder.appartment.unit.RoomChat

class RoomChatAdapter(
    var context: Context,
    var msgList: ArrayList<RoomChat>,
    val clickCallback: PostAdapter.PostClickCallback
) : RecyclerView.Adapter<RoomChatAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding : ItemRoomChatBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRoomChatBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.name.text = RoomChat.splitOtherName(msgList[position].roomName)
        val msg = msgList[position].messages
        if (msg.size > 0) {
            val fromUser = if (msg[msg.size - 1].owner == Application.username) "You: " else ""
            holder.itemBinding.lastMsg.text = fromUser + msg[msg.size - 1].message
            if (!msg[msg.size - 1].seen && Application.username != msg[msg.size - 1].owner) {
                holder.itemBinding.lastMsg.setTextColor(Color.parseColor("#0168f6"))
            } else {
                holder.itemBinding.lastMsg.setTextColor(Color.parseColor("#353535"))
            }
        }
        holder.itemBinding.root.setOnClickListener {
            clickCallback.onPostClick(position)
        }
    }
}