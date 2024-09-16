package com.example.core.domain.model

data class DetailDisease(
	val msg: String? = null,
	val data: List<DataItem>,
	val status: Int? = null
)

data class DataItem(
	val symptoms: String? = null,
	val treatment: String? = null,
	val userId: Int? = null,
	val imageUrl: String? = null,
	val confidenceScore: Any? = null,
	val cause: String? = null,
	val id: Int? = null,
	val category: String? = null,
	val detectionDate: String? = null
)

