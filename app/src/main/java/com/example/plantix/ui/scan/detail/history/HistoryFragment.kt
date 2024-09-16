package com.example.plantix.ui.scan.detail.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.Detection
import com.example.core.domain.model.DataItem
import com.example.core.domain.model.ItemDetection
import com.example.core.ui.HistoryDetectionAdapter
import com.example.core.ui.ListDetectionAdapter
import com.example.plantix.R
import com.example.plantix.databinding.FragmentHistoryBinding
import com.example.plantix.ui.scan.DetectionViewModel
import com.example.plantix.ui.scan.detail.DetailScanActivity
import com.example.plantix.ui.scan.detail.bottom.BottomSheetDialog
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _historyFragment: FragmentHistoryBinding? = null

    private val historyFragment get() = _historyFragment!!

    private val detectionViewModel: DetectionViewModel by viewModel()

    private var plantId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _historyFragment = FragmentHistoryBinding.inflate(inflater, container, false)
        return _historyFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        historyScanResult()
    }

    private fun observeData() {
        plantId = arguments?.getInt("PLANT_ID")
        Log.d("OverviewFragment", "id: $plantId")

        plantId?.let { detectionViewModel.detailDetection(it) }
    }

    private fun historyScanResult() {
        detectionViewModel.detectionDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val data = it.data.data
                    val sortedData = data.sortedByDescending { plant -> plant.detectionDate }
                    showListHistory(sortedData)
                }

                is Resource.Error -> {
                    Log.d("OverviewFragment", "error: ${it.message}")
                }
            }
        }
    }

    private fun showListHistory(data: List<DataItem>) {
        historyFragment.rvHistoryScan.apply {
            adapter = HistoryDetectionAdapter(data) {
                val bottomSheet = BottomSheetDialog()
                bottomSheet.setPlantData(it)
                bottomSheet.show(parentFragmentManager, "BottomSheetDialog")
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _historyFragment = null
    }

    override fun onResume() {
        super.onResume()
        observeData()
        historyScanResult()
    }

}