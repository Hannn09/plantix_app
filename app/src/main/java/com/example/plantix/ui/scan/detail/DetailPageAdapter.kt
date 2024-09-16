package com.example.plantix.ui.scan.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.plantix.ui.scan.detail.history.HistoryFragment
import com.example.plantix.ui.scan.detail.overview.OverviewFragment

class DetailPageAdapter(activity: AppCompatActivity, private val plantId: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt("PLANT_ID", plantId)
                }
            }
            1 -> HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt("PLANT_ID", plantId)
                }
            }
            else -> throw IllegalArgumentException("Invalid position fragment")
        }
    }
}