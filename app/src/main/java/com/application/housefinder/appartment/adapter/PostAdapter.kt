package com.application.housefinder.appartment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.housefinder.appartment.databinding.ItemPostBinding
import com.application.housefinder.appartment.unit.Post
import com.bumptech.glide.Glide

class PostAdapter(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    class ViewHolder(val itemBinding: ItemPostBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface PostClickCallback {
        fun onPostClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPostBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.tvTitle.text = postList[position].title
        holder.itemBinding.tvContent.text = postList[position].description
        if (postList[position].imgURLs.isNotEmpty()) {
            holder.itemBinding.rlImg.visibility = View.VISIBLE
            Glide.with(context).load(postList[position].imgURLs[0])
                .into(holder.itemBinding.thumbnail)

            if (postList[position].imgURLs.size > 1) {
                holder.itemBinding.rlMulti.visibility = View.VISIBLE
                holder.itemBinding.tvCountImgs.text = "+" + (postList[position].imgURLs.size - 1)
            } else {
                holder.itemBinding.rlMulti.visibility = View.GONE
            }

        } else {
            holder.itemBinding.rlImg.visibility = View.GONE
        }

    }
}


