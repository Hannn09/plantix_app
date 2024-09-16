package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: List<DetailUser?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DetailUser(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("password_hash")
	val passwordHash: String? = null,

	@field:SerializedName("profile_picture_url")
	val profilePictureUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
