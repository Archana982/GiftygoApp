@file:Suppress("DEPRECATION")

package com.example.giftygo.rvadapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giftygo.Models.ShoeDisplayModel
import com.example.giftygo.databinding.ShoedisplaymainItemBinding


class ShoeDisplayAdapter(
  private val context: Context,
  private val list: List<ShoeDisplayModel>,
  private val productClickInterface: ProductOnClickInterface,
  private val likeClickInterface: LikeOnClickInterface,
) : RecyclerView.Adapter<ShoeDisplayAdapter.ViewHolder>() {

  inner class ViewHolder(val binding: ShoedisplaymainItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ShoedisplaymainItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val currentItem = list[position]

    // Set product name and price
    holder.binding.tvNameShoeDisplayItem.text = " ${currentItem.name}"

    // Load product image using Glide
    Glide.with(context)
      .load(currentItem.imgUrl)
      .into(holder.binding.ivShoeDisplayItem)

    // Like button logic
    holder.binding.btnLike.setOnClickListener {
      if (holder.binding.btnLike.isChecked) {
        holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.RED)
        likeClickInterface.onClickLike(currentItem)
      } else {
        holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
      }
    }

    // On product click -> navigate to DetailFragment
    holder.itemView.setOnClickListener {
      productClickInterface.onClickProduct(currentItem)
    }
  }

  override fun getItemCount(): Int = list.size
}

interface ProductOnClickInterface {
  fun onClickProduct(item: ShoeDisplayModel)
}

interface LikeOnClickInterface {
  fun onClickLike(item: ShoeDisplayModel)
}