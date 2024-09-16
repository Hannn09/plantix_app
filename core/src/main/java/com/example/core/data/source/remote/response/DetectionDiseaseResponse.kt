package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetectionDiseaseResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: DataDetectionResponse? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Detection(

	@field:SerializedName("namePlant")
	val namePlant: String? = null,

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

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("detection_date")
	val detectionDate: String? = null
)

data class DataDetectionResponse(

	@field:SerializedName("detection")
	val detection: Detection? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null
)

data class RequestDataDetection(
	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("plantId")
	val plantId: Int? = null
)
