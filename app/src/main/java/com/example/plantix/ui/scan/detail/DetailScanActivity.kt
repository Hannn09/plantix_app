package com.example.plantix.ui.scan.detail

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.plantix.R
import com.example.plantix.databinding.ActivityDetailScanBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class DetailScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        setupView()

    }

    private fun setupView() {
//        val barChart = binding.barChart
//        val entriesGroup1 = listOf(
//            BarEntry(0f, 75f),
//            BarEntry(1f, 55f),
//            BarEntry(2f, 80f),
//            BarEntry(3f, 60f),
//            BarEntry(4f, 90f),
//            BarEntry(5f, 100f),
//            BarEntry(6f, 85f)
//        )
//
//        val entriesGroup2 = listOf(
//            BarEntry(0f, 60f),
//            BarEntry(1f, 50f),
//            BarEntry(2f, 70f),
//            BarEntry(3f, 40f),
//            BarEntry(4f, 75f),
//            BarEntry(5f, 90f),
//            BarEntry(6f, 80f)
//        )
//
//        val entriesGroup3 = listOf(
//            BarEntry(0f, 70f),
//            BarEntry(1f, 45f),
//            BarEntry(2f, 60f),
//            BarEntry(3f, 35f),
//            BarEntry(4f, 80f),
//            BarEntry(5f, 85f),
//            BarEntry(6f, 70f)
//        )
//
//        val barDataSet1 = BarDataSet(entriesGroup1, "Warna Daun").apply {
//            color = Color.GREEN
//        }
//        val barDataSet2 = BarDataSet(entriesGroup2, "Warna Batang").apply {
//            color = Color.YELLOW
//        }
//        val barDataSet3 = BarDataSet(entriesGroup3, "Kualitas Bunga").apply {
//            color = Color.RED
//        }
//        val barData = BarData(barDataSet1, barDataSet2, barDataSet3)
//        barData.barWidth = 0.2f
//
//        barChart.data = barData
//
//        barChart.xAxis.apply {
//            position = XAxis.XAxisPosition.BOTTOM
//            granularity = 1f
//            setDrawGridLines(false)
//            valueFormatter = object : ValueFormatter() {
//                private val days = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
//                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                    return days.getOrNull(value.toInt()) ?: value.toString()
//                }
//            }
//        }
//
//        barChart.axisLeft.apply {
//            axisMinimum = 0f
//        }
//        barChart.axisRight.isEnabled = false
//        barChart.description = Description().apply {
//            text = ""
//        }
//        barChart.setFitBars(true)
//        barChart.animateY(1000)
//        barChart.invalidate()
    }

    private fun setupListener() {
//        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}