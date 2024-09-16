package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListDetectionResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: List<DataDetectionItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataDetectionItem(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("plant_id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("detection_date")
	val detectionDate: String? = null,

	@field:SerializedName("nama")
	val namePlant: String? = null,
)
