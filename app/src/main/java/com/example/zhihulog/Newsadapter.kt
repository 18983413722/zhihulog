package com.example.zhihulog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Newsadapter :ListAdapter<StoryItem, Newsadapter.STORYViewHolder>(NewsDiffCallback){

    var onItewClickListener : ((StoryItem,Int) -> Unit)? = null

    inner class STORYViewHolder(view: View):RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener{
                val position = adapterPosition
                onItewClickListener?.invoke(getItem(position),position)
            }
        }

        val title : TextView = view.findViewById(R.id.titleTextView)
        val author : TextView = view.findViewById(R.id.authorTextView)
        val image : ImageView = view.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Newsadapter.STORYViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview,parent,false)
        return  STORYViewHolder(view)
    }

    override fun onBindViewHolder(holder: Newsadapter.STORYViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.author.text = item.hint
        if (!item.imageUrl.isNullOrEmpty()){
            Glide.with(holder.itemView)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.image)
        }else{
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        }
        }


    object NewsDiffCallback : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
                }
            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    override fun getItemCount(): Int {
        Log.d("Adapter", "当前数据量: ${super.getItemCount()}")
        return super.getItemCount()
    }
}