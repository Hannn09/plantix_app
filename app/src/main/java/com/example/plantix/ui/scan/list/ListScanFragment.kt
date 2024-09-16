package com.example.plantix.ui.scan.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.domain.model.ItemDetection
import com.example.core.ui.ListDetectionAdapter
import com.example.plantix.R
import com.example.plantix.databinding.FragmentListScanBinding
import com.example.plantix.ui.auth.login.LoginViewModel
import com.example.plantix.ui.scan.DetectionViewModel
import com.example.plantix.ui.scan.detail.DetailScanActivity
import com.example.plantix.ui.scan.detection.ScanActivity
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel


class ListScanFragment : Fragment() {

    private var _scanFragment: FragmentListScanBinding? = null

    private val loginViewModel: LoginViewModel by viewModel()

    private val detectionViewModel: DetectionViewModel by viewModel()

    private var userId: Int? = null

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private lateinit var scanActivityResultLauncher: ActivityResultLauncher<Intent>

    private val scanFragment get() = _scanFragment!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _scanFragment = FragmentListScanBinding.inflate(inflater, container, false)
        return _scanFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    observeData()
                }
            }

        shimmerFrameLayout = scanFragment.shimmerLayout
        observeData()
        setupListener()
    }

    private fun observeData() {
        loginViewModel.getUserId()
        loginViewModel.userId.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    userId = it.data
                    detectionViewModel.getUserDetection(it.data)
                }

                is Resource.Error -> {}
            }
        }
        listDetectionResult()
    }

    private fun listDetectionResult() {
        detectionViewModel.listUserDetection.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    val data = it.data.data

                    val latestData = data
                        .groupBy { plant -> plant.id }
                        .mapValues { entry -> entry.value.maxByOrNull { it.detectionDate.toString() } }
                        .values.filterNotNull()

                    val latestScanned = latestData.sortedByDescending { plant -> plant.detectionDate }

                    scanFragment.swipeRefresh.isRefreshing = false
                    scanFragment.countPlant.text = latestData.size.toString()
                    if (latestData.isNotEmpty()) {
                        showContent(false)
                        showListDetection(latestScanned)
                    } else {
                        showContent(true)
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showContent(true)
                    scanFragment.swipeRefresh.isRefreshing = false
                    Log.d("ListScanFragment", "error: ${it.message}")
                }
            }
        }
    }

    private fun showListDetection(data: List<ItemDetection>) {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            requireContext(),
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        scanFragment.rvItemScan.apply {
            adapter = ListDetectionAdapter(data) {
                val plantId = it.id
                val plantName = it.namePlant

                val intent = Intent(requireContext(), DetailScanActivity::class.java)
                intent.putExtra("PLANT_ID", plantId)
                intent.putExtra("NAME_PLANT", plantName)
                startActivity(intent, options.toBundle())
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListener() {
        scanFragment.apply {
            icScan.setOnClickListener {
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                val intent = Intent(requireContext(), ScanActivity::class.java)
                intent.putExtra("SOURCE", "fragment")
                scanActivityResultLauncher.launch(intent, options)
            }

            swipeRefresh.setOnRefreshListener {
                showLoading(true)
                observeData()
            }
            swipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.primary_color))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        scanFragment.apply {
            if (isLoading) {
                shimmerFrameLayout.startShimmer()
                shimmerFrameLayout.visibility = View.VISIBLE
                showContent(false)
                rvItemScan.visibility = View.GONE
            } else {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                rvItemScan.visibility = View.VISIBLE
            }
        }
    }

    private fun showContent(isShowing: Boolean) {
        scanFragment.let {
            it.notFoundContent.layout.visibility = if (isShowing) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _scanFragment = null
    }

}