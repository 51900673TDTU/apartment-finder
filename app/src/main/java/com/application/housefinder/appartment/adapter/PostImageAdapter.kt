package com.application.housefinder.appartment.adapter

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.application.housefinder.appartment.databinding.ItemImageBinding
import com.bumptech.glide.Glide


class PostImageAdapter(
    val context: Context,
    var imgList: List<String>,
    var itemClickListener: PostAdapter.PostClickCallback
) : RecyclerView.Adapter<PostImageAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ItemImageBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemBinding.rlAddPhoto.visibility = View.GONE
        Glide.with(context).load(imgList[position]).into(holder.itemBinding.img)
        holder.itemBinding.imgClose.visibility = View.GONE


        holder.itemBinding.root.setOnClickListener {
            itemClickListener.onPostClick(position)
        }

    }
}