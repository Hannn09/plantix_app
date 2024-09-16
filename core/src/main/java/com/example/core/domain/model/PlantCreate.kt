package com.example.core.domain.model

data class PlantCreate(
	val msg: String? = null,
	val data: Data? = null,
	val status: Int? = null
)

data class Data(
	val nama: String? = null,
	val userId: Int? = null,
	val id: Int? = null
)

