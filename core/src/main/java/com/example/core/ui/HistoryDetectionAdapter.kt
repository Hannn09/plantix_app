package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.core.R
import com.example.core.databinding.ItemHistoryScanBinding
import com.example.core.domain.model.DataItem
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.logging.SimpleFormatter

class HistoryDetectionAdapter(private val historyList: List<DataItem>, private val onClick: (DataItem) -> Unit) : RecyclerView.Adapter<HistoryDetectionAdapter.HistoryDetectionViewHolder>() {
    inner class HistoryDetectionViewHolder(private val binding: ItemHistoryScanBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            binding.apply {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
                val formatter = SimpleDateFormat("d.M.yyyy", Locale.getDefault())
                val date = inputFormat.parse(data.detectionDate)

                val category = when(data.category) {
                    "Bacteria" -> "Bakteri"
                    "Pests" -> "Pestisida"
                    "Virus" -> "Virus"
                    "Fungus" -> "Fungus"
                    "Healthy" -> "Sehat"
                    else -> data.category
                }

                tvIdScan.text = "#PX${data.id}"
                tvIndicatorScan.text = category

                if (category != "Sehat") {
                    tvIndicatorScan.setTextColor(ContextCompat.getColor(binding.root.context,R.color.plant_detection))
                } else {
                    tvIndicatorScan.setTextColor(ContextCompat.getColor(binding.root.context,R.color.primary_color))
                }

                date?.let {
                    val formattedDate = formatter.format(it)
                    tvDateScan.text = formattedDate
                }

                root.setOnClickListener { onClick(data) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetectionViewHolder {
       val binding = ItemHistoryScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryDetectionViewHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: HistoryDetectionViewHolder, position: Int) {
        holder.bind(historyList[position])
    }
}