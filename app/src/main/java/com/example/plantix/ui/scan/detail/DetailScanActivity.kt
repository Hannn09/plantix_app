package com.example.plantix.ui.scan.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.core.domain.model.DataItem
import com.example.plantix.R
import com.example.plantix.databinding.ActivityDetailScanBinding
import com.example.plantix.ui.scan.DetectionViewModel
import com.example.plantix.ui.scan.detection.ScanActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DetailScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScanBinding

    private var plantId: Int? = null

    private var plantName: String? = null

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private val detectionViewModel: DetectionViewModel by viewModel()

    private lateinit var scanActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scanActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                observeData()
            }
        }

        shimmerFrameLayout = binding.shimmerLayout
        observeData()
        detailScanResult()
        setupView()
        setupListener()
    }

    private fun observeData() {
        plantId = intent?.getIntExtra("PLANT_ID", 0)
        plantName = intent?.getStringExtra("NAME_PLANT")
        Log.d("DetailScanActivity", "plant: $plantName")

        plantId?.let { detectionViewModel.detailDetection(it) }
    }

    private fun detailScanResult() {
        detectionViewModel.detectionDetail.observe(this) {
            when (it) {
                is Resource.Loading -> {showLoading(true)}
                is Resource.Success -> {
                    showLoading(false)
                    val data = it.data.data
                    if (data.isNotEmpty()) {
                        val latestData = data.maxByOrNull { item -> item.detectionDate.toString() }
                        latestData?.let { item -> setupContent(item) }
                        Log.d("DetailScanActivity", "data: $latestData")

                    } else {
                        Log.d("DetailScanActivity", "data list is empty or null")
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    Log.d("DetailScanActivity", "error: ${it.message}")
                }
            }
        }
    }

    private fun setupContent(data: DataItem?) {
        binding.apply {
            Glide.with(this@DetailScanActivity)
                .load(data?.imageUrl)
                .placeholder(com.example.core.R.drawable.dummy_img_item)
                .into(imgPlant)

            scientificPlant.text = "Kategori Penyakit : ${data?.category}"
            val capitalizedPlantName = plantName
                ?.split(" ")
                ?.joinToString(separator = " ") { it.capitalize() }

            val plantNameWithPrefix = if (capitalizedPlantName?.contains("Tanaman", ignoreCase = true) == true) {
                capitalizedPlantName
            } else {
                "Tanaman $capitalizedPlantName"
            }

            namePlant.text = plantName

        }
    }

    private fun setupListener() {
        binding.apply {
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this@DetailScanActivity,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            icBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            icScan.setOnClickListener {
                val intent = Intent(this@DetailScanActivity, ScanActivity::class.java)
                intent.putExtra("SOURCE", "detail")
                intent.putExtra("PLANT_NAME", plantName)
                intent.putExtra("PLANT_ID", plantId)
                scanActivityResultLauncher.launch(intent, options)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            shimmerFrameLayout.startShimmer()
            shimmerFrameLayout.visibility = View.VISIBLE
            binding.contentDetail.visibility = View.GONE
        } else {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            binding.contentDetail.visibility = View.VISIBLE
        }
    }

    private fun setupView() {
        binding.apply {
            val adapter = plantId?.let { DetailPageAdapter(this@DetailScanActivity, it) }
            viewPager.adapter = adapter
            val tabLayout = tabs
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.overview)
                    1 -> getString(R.string.tab_scan)
                    else -> throw IllegalArgumentException("Invalid position fragment")
                }
            }.attach()
        }
    }
}
