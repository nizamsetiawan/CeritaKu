package com.bangkit.intermediate.ceritaku.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.intermediate.ceritaku.databinding.ItemstoryBinding
import com.bangkit.intermediate.ceritaku.source.response.ListStoryItem
import com.bangkit.intermediate.ceritaku.ui.home.DetailStoryActivity
import com.bumptech.glide.Glide

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(StoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemstoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemstoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            binding.apply {
                nameTextView.text = item.name
                descriptionTextView.text = item.description
                Glide.with(root.context)
                    .load(item.photoUrl)
                    .into(photoImageView)

                root.setOnClickListener {
                    val intent = Intent(root.context, DetailStoryActivity::class.java)
                    intent.putExtra("story_id", item.id)
                    root.context.startActivity(intent)
                }
            }
        }
    }

    object StoryDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
