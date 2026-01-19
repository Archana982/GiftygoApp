package com.example.giftygo.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giftygo.Models.LikeModel
import com.example.giftygo.R

class LikeAdapter(
    private val likeList: MutableList<LikeModel>,
    private val onClickLike: (LikeModel) -> Unit,
    private val onItemClick: (LikeModel) -> Unit
) : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>() {

    inner class LikeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvLikeName)
        val image: ImageView = view.findViewById(R.id.ivLikeImage)
        val likeButton: ImageView = view.findViewById(R.id.ivLike)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.like_item, parent, false)
        return LikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val item = likeList[position]
        holder.name.text = item.name

        Glide.with(holder.itemView.context)
            .load(item.imgUrl)
            .placeholder(R.drawable.custom_image_background)
            .into(holder.image)

        holder.likeButton.setOnClickListener {
            onClickLike(item)
        }

        // ✅ Item click to navigate
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = likeList.size
}
