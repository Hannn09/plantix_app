package com.example.core.domain.model

import com.google.gson.annotations.SerializedName

data class News(
	val totalResults: Int? = null,
	val articles: List<ArticlesItem>,
	val status: String? = null
)

data class ArticlesItem(
	val publishedAt: String? = null,
	val author: String? = null,
	val urlToImage: String? = null,
	val description: String? = null,
	val source: Source? = null,
	val title: String? = null,
	val url: String? = null,
	val content: String? = null
)

data class Source(
	val name: String? = null,
	val id: Any? = null
)
