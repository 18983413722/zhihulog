package com.example.zhihulog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BannerAdapter : ListAdapter<BannerItem, BannerAdapter.BannerViewHolder>(BannerDiffCallback){
    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image : ImageView = view.findViewById(R.id.BannerImage)
        val title : TextView = view.findViewById(R.id.BannerTitle)
        val anthor : TextView = view.findViewById(R.id.BannerAnthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bannerview,parent,false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerAdapter.BannerViewHolder, position: Int) {
        val item =getItem(position)
        holder.title.text = item.title
        holder.anthor.text = item.hint
        if (!item.imageUrl.isNullOrEmpty()){
            Glide.with(holder.itemView)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image)
        }else{
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
    object BannerDiffCallback : DiffUtil.ItemCallback<BannerItem>() {
        override fun areItemsTheSame(oldItem: BannerItem, newItem: BannerItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BannerItem, newItem: BannerItem) =
            oldItem == newItem
    }
}