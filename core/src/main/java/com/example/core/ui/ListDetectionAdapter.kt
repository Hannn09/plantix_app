package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.databinding.ItemScanPlantBinding
import com.example.core.domain.model.ItemDetection

class ListDetectionAdapter(private val detectionList: List<ItemDetection>, private val onClick: (ItemDetection) -> Unit) : RecyclerView.Adapter<ListDetectionAdapter.ListDetectionViewHolder>() {
   inner class ListDetectionViewHolder(private val binding: ItemScanPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ItemDetection) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .placeholder(R.drawable.dummy_img_item)
                    .into(imgPlant)
                namePlant.text = data.namePlant?.split(" ")?.joinToString(separator = " ") { it.capitalize() }
                scientificPlant.text = data.category
                root.setOnClickListener { onClick(data) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDetectionViewHolder {
        val binding = ItemScanPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListDetectionViewHolder(binding)
    }

    override fun getItemCount(): Int = detectionList.size

    override fun onBindViewHolder(holder: ListDetectionViewHolder, position: Int) {
       holder.bind(detectionList[position])
    }
}