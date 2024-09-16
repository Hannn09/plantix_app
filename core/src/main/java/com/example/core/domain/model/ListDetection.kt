package com.example.core.domain.model

data class ListDetection(
	val msg: String? = null,
	val data: List<ItemDetection>,
	val status: Int? = null
)

data class ItemDetection(
	val userId: Int? = null,
	val imageUrl: String? = null,
	val id: Int? = null,
	val category: String? = null,
	val detectionDate: String? = null,
	val namePlant: String? = null
)

