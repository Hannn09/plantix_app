package com.example.plantix.ui.scan.detail.overview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.core.domain.model.DataItem
import com.example.plantix.R
import com.example.plantix.databinding.FragmentOverviewBinding
import com.example.plantix.ui.scan.DetectionViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class OverviewFragment : Fragment() {

    private var _overviewFragment: FragmentOverviewBinding? = null

    private val overviewFragment get() = _overviewFragment!!

    private val detectionViewModel: DetectionViewModel by viewModel()

    private var plantId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _overviewFragment = FragmentOverviewBinding.inflate(inflater,  container, false)
        return _overviewFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        detailScanResult()
    }

    private fun observeData() {
        plantId = arguments?.getInt("PLANT_ID")
        Log.d("OverviewFragment", "id: $plantId")

        plantId?.let { detectionViewModel.detailDetection(it) }
    }

    private fun detailScanResult() {
        detectionViewModel.detectionDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val data = it.data.data

                    if (data.isNotEmpty()) {
                        val latestData = data.maxByOrNull { item -> item.detectionDate.toString() }
                        latestData?.let { item -> setupView(item) }
                    } else {
                        Log.d("OverviewFragment", "data list is empty or null")
                    }
                }

                is Resource.Error -> {
                    Log.d("OverviewFragment", "error: ${it.message}")
                }
            }
        }
    }

    private fun setupView(data: DataItem) {
        overviewFragment.apply {
            val category = when(data.category) {
                "Bacteria" -> "Bakteri"
                "Pests" -> "Pestisida"
                "Virus" -> "Virus"
                "Fungus" -> "Fungus"
                "Healthy" -> "Sehat"
                else -> data.category
            }
            cardInformation.apply {
                Glide.with(requireContext())
                    .load(data.imageUrl)
                    .placeholder(com.example.core.R.drawable.dummy_img_item)
                    .into(imgPlant)
                if (data.category != "Healthy") {
                    desc.text = "terkena"
                    information.setBackgroundResource(R.drawable.bg_info_non_health)
                    plantDescription.text = category
                    plantDescription.setTextColor(ContextCompat.getColor(requireContext(),R.color.plant_detection))

                } else {
                    plantDescription.text = category
                }
            }

            if (data.category == "Healthy") {
                disease.visibility = View.GONE
                name.visibility = View.GONE
                nameDisease.visibility = View.GONE
                cause.visibility = View.GONE
                causeDisease.visibility = View.GONE
                symptoms.visibility = View.GONE
                symptomsDisease.visibility = View.GONE
            } else {
                nameDisease.text = category
                val causeList = data.cause?.split(";")
                val formattedCause = causeList?.joinToString("\n") { "- $it" }
                causeDisease.text = formattedCause

                val symptomsList = data.symptoms?.split(";")
                val formattedSymptoms = symptomsList?.joinToString("\n") { "- $it" }
                symptomsDisease.text = formattedSymptoms

                val treatmentList = data.treatment?.split(";")
                val formattedTreatment = treatmentList?.joinToString("\n") { "- $it" }
                carePlant.text = formattedTreatment
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _overviewFragment = null
    }

    override fun onResume() {
        super.onResume()
        observeData()
        detailScanResult()
    }

}