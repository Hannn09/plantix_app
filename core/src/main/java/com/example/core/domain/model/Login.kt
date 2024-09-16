package com.example.core.domain.model

data class Login(
    val accessToken: String? = null,
    val username: String,
    val userId: Int,
)
