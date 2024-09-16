package com.example.core.domain.model

data class DetectionDisease(
	val msg: String? = null,
	val data: DataDetection? = null,
	val status: Int? = null
)

data class DataDetection(
	val detection: DetectionItem? = null,
	val imageUrl: String? = null
)

data class DetectionItem(
	val symptoms: String? = null,
	val treatment: String? = null,
	val userId: Int? = null,
	val imageUrl: String? = null,
	val confidenceScore: Any? = null,
	val cause: String? = null,
	val id: Int? = null,
	val category: String? = null,
	val namePlant: String? = null,
	val detectionDate: String? = null
)

