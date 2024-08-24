package com.example.core.utils

import com.example.core.data.source.remote.response.LoginResponse
import com.example.core.data.source.remote.response.NewsResponse
import com.example.core.data.source.remote.response.RegisterResponse
import com.example.core.domain.model.ArticlesItem
import com.example.core.domain.model.Login
import com.example.core.domain.model.News
import com.example.core.domain.model.Register
import com.example.core.domain.model.Source
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DataMapper {

    fun newsResponseToNews(data: NewsResponse) : News = News(
        status = data.status,
        totalResults = data.totalResults,
        articles = data.articles!!.map {
            ArticlesItem(
                publishedAt = it?.publishedAt,
                author = it?.author,
                urlToImage = it?.urlToImage,
                description = it?.description,
                title = it?.title,
                url = it?.url,
                content = it?.content,
                source = it?.source?.let { source ->
                    Source(
                        name = source.name,
                        id = source.id
                    )
                }
            )
        }
    )

    fun parseDatePublishToDate(dateString: String?) : String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date? = dateString?.let { inputFormat.parse(it) }
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun registerResponseToRegister(data: RegisterResponse) : Register = Register(data.message)

    fun loginResponseToLogin(data: LoginResponse) : Login = Login(accessToken = data.accessToken, username = data.username)

}