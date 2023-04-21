package com.application.housefinder.appartment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.housefinder.appartment.databinding.ItemPostBinding
import com.application.housefinder.appartment.unit.Post

class PostAdapter(var context : Context, var postList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    class ViewHolder(val itemBinding : ItemPostBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPostBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = holder.itemBinding.root
    }


}