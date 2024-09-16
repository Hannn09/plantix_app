package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailDiseaseResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: List<DetailItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DetailItem(

	@field:SerializedName("symptoms")
	val symptoms: String? = null,

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("confidence_score")
	val confidenceScore: Any? = null,

	@field:SerializedName("cause")
	val cause: String? = null,

	@field:SerializedName("plant_id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("detection_date")
	val detectionDate: String? = null
)
