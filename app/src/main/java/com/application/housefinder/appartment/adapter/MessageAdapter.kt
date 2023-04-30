package com.application.housefinder.appartment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.housefinder.appartment.Application
import com.application.housefinder.appartment.databinding.ItemMessageBinding
import com.application.housefinder.appartment.unit.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


class MessageAdapter(
    var context: Context,
    var msgList: ArrayList<Message>,
    val clickCallback: PostAdapter.PostClickCallback
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding : ItemMessageBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateTime = msgList[position].date

        if (position == 0) {
            holder.itemBinding.date.visibility = View.VISIBLE
            holder.itemBinding.date.text = SimpleDateFormat("EEEE, hh:mm aa", Locale("en")).format(dateTime)
        } //Check if new day
        else if (convertInstanceToDate(dateTime) != convertInstanceToDate(msgList[position - 1].date)) {
            holder.itemBinding.date.visibility = View.VISIBLE
            holder.itemBinding.date.text = SimpleDateFormat("EEEE, hh:mm aa", Locale("en")).format(dateTime)
        } else {
            holder.itemBinding.date.visibility = View.GONE
        }

        if (msgList[position].owner == Application.username) {
            //If a message is sent
            holder.itemBinding.sentLayout.visibility = View.VISIBLE
            holder.itemBinding.sentTextView.text = msgList[position].message
            holder.itemBinding.receivedLayout.visibility = View.GONE
        } else {
            //Message is received
            holder.itemBinding.receivedLayout.visibility = View.VISIBLE
            holder.itemBinding.receivedTextView.text = msgList[position].message;
            // Set visibility as GONE to remove the space taken up
            holder.itemBinding.sentLayout.visibility = View.GONE

        }


    }

    var DAY_IN_MILLIS: Long = 86400000

    fun convertInstanceToDate(time: Long): Long {
        return floor(time * 1.0 / DAY_IN_MILLIS).toLong() * DAY_IN_MILLIS
    }

}