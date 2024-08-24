package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.databinding.LayoutNewsBinding
import com.example.core.domain.model.ArticlesItem
import com.example.core.utils.DataMapper

class BannerNewsAdapter(private val newsItem: List<ArticlesItem>, private val onClick: (ArticlesItem) -> Unit) : RecyclerView.Adapter<BannerNewsAdapter.BannerNewsHolder>() {
    inner class BannerNewsHolder(private val binding: LayoutNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticlesItem) {
            binding.apply {
                titleNews.text = data.title
                author.text = "${data.author} · ${DataMapper.parseDatePublishToDate(data.publishedAt)}"
                Glide.with(itemView.context)
                    .load(data.urlToImage)
                    .placeholder(R.drawable.banner_dummy)
                    .into(imageNews)
                root.setOnClickListener { onClick(data) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerNewsHolder {
        val binding = LayoutNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerNewsHolder(binding)
    }

    override fun getItemCount(): Int = newsItem.size

    override fun onBindViewHolder(holder: BannerNewsHolder, position: Int) {
        holder.bind(newsItem[position])
    }
}