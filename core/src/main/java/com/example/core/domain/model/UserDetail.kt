package com.example.core.domain.model

data class UserDetail(
	val msg: String? = null,
	val data: List<DataUser?>? = null,
	val status: Int? = null
)

data class DataUser(
	val fullName: String? = null,
	val updatedAt: String? = null,
	val passwordHash: String? = null,
	val profilePictureUrl: String? = null,
	val createdAt: String? = null,
	val id: Int? = null,
	val email: String? = null,
	val username: String? = null
)

