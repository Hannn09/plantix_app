package com.example.plantix.ui.scan.detail.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.domain.model.DataItem
import com.example.plantix.databinding.BottomSheetDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDetailBinding? = null

    private val binding get() = _binding!!

    private var plantData: DataItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantData?.let {
            val category = when(it.category) {
                "Bacteria" -> "Bakteri"
                "Pests" -> "Pestisida"
                "Virus" -> "Virus"
                "Fungus" -> "Fungus"
                "Healthy" -> "Sehat"
                else -> it.category
            }
            binding.apply {
                val treatmentList = it.treatment?.split(";")
                val formattedTreatment = treatmentList?.joinToString("\n") { "- $it" }
                carePlant.text = formattedTreatment

                val symptomsList = it.symptoms?.split(";")
                val formattedSymptoms = symptomsList?.joinToString("\n") { "- $it" }
                symptomsPlant.text = formattedSymptoms

                val causeList = it.cause?.split(";")
                val formattedCause = causeList?.joinToString("\n") { "- $it" }
                causePlant.text = formattedCause

                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .placeholder(R.drawable.dummy_img_item)
                    .into(imgPlant)

                if (category != "Healthy") {
                    information.setBackgroundResource(com.example.plantix.R.drawable.bg_info_non_health)
                    informationPlant.text = "Tanaman terkena $category. Pantau terus kesehatan tanamanmu!"
                } else {
                    cause.visibility = View.GONE
                    causePlant.visibility = View.GONE
                    symptoms.visibility = View.GONE
                    symptomsPlant.visibility = View.GONE
                    information.setBackgroundResource(com.example.plantix.R.drawable.bg_info_health)
                    informationPlant.text = "Tanaman terlihat $category. Pantau terus kesehatan tanamanmu!"
                }

                binding.icClose.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    fun setPlantData(dataItem: DataItem) {
        plantData = dataItem
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}