package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemNewsBinding
import com.example.core.domain.model.ArticlesItem
import com.example.core.domain.model.News
import com.example.core.utils.DataMapper

class NewsItemAdapter(private val newsItem: List<ArticlesItem>, private val onClick: (ArticlesItem) -> Unit) : RecyclerView.Adapter<NewsItemAdapter.NewsItemHolder>() {
    inner class NewsItemHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticlesItem) {
            binding.apply {
                title.text = data.title
                author.text = "${data.author} · ${DataMapper.parseDatePublishToDate(data.publishedAt)}"
                Glide.with(itemView.context)
                    .load(data.urlToImage)
                    .into(thumbnail)
                root.setOnClickListener { onClick(data) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemHolder(binding)
    }

    override fun getItemCount(): Int = newsItem.size

    override fun onBindViewHolder(holder: NewsItemHolder, position: Int) {
        holder.bind(newsItem[position])
    }
}