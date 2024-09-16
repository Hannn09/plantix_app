package com.example.core.utils

import com.example.core.data.source.remote.response.DetailDiseaseResponse
import com.example.core.data.source.remote.response.DetailUser
import com.example.core.data.source.remote.response.DetectionDiseaseResponse
import com.example.core.data.source.remote.response.ListDetectionResponse
import com.example.core.data.source.remote.response.LoginResponse
import com.example.core.data.source.remote.response.NewsResponse
import com.example.core.data.source.remote.response.PlantCreateResponse
import com.example.core.data.source.remote.response.RegisterResponse
import com.example.core.data.source.remote.response.RequestDataDetection
import com.example.core.data.source.remote.response.UserDetailResponse
import com.example.core.domain.model.ArticlesItem
import com.example.core.domain.model.Data
import com.example.core.domain.model.DataDetection
import com.example.core.domain.model.DataItem
import com.example.core.domain.model.DataUser
import com.example.core.domain.model.DetailDisease
import com.example.core.domain.model.DetectionDisease
import com.example.core.domain.model.DetectionItem
import com.example.core.domain.model.ItemDetection
import com.example.core.domain.model.ListDetection
import com.example.core.domain.model.Login
import com.example.core.domain.model.News
import com.example.core.domain.model.PlantCreate
import com.example.core.domain.model.Register
import com.example.core.domain.model.Source
import com.example.core.domain.model.UserDetail
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DataMapper {

    fun newsResponseToNews(data: NewsResponse): News = News(
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

    fun parseDatePublishToDate(dateString: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date? = dateString?.let { inputFormat.parse(it) }
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun registerResponseToRegister(data: RegisterResponse): Register = Register(data.message)

    fun loginResponseToLogin(data: LoginResponse): Login =
        Login(accessToken = data.accessToken, username = data.username, userId = data.userId)

    fun detailUsersResponseToDetailUser(data: UserDetailResponse) : UserDetail = UserDetail(
        data = data.data?.map {
            DataUser(
                username = it?.username,
                email = it?.email,
                profilePictureUrl = it?.profilePictureUrl,
                fullName = it?.fullName
            )
        }
    )

    fun updateUserResponseToUser(data: UserDetailResponse): UserDetail = UserDetail(msg = data.msg)

    fun updateUserMapping(data: DetailUser): Map<String, RequestBody> {
        val requestBodyMap = mutableMapOf<String, RequestBody>()

        requestBodyMap["full_name"] = data.fullName.toString().toRequestBody()
        data.profilePictureUrl?.let {
            val file = File(it)
            if (file.exists()) {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                requestBodyMap["profile_picture\"; filename=\"${file.name}"] = requestFile
            }
        }
        return requestBodyMap
    }

    fun requestDetection(data: RequestDataDetection): Map<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()

        map["userId"] = data.userId.toString().toRequestBody()
        map["plantId"] = data.plantId.toString().toRequestBody()
        data.image?.let {
            val file = File(it)
            if (file.exists()) {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                map["image\"; filename=\"${file.name}"] = requestFile
            }
        }

        return map
    }

    fun requestDetectionResponse(data: DetectionDiseaseResponse): DetectionDisease =
        DetectionDisease(
            msg = data.msg,
            data = data.data?.let { detection ->
                DataDetection(
                    imageUrl = detection.imageUrl,
                    detection = detection.detection?.let {
                        DetectionItem(
                            namePlant = it.namePlant,
                            symptoms = it.symptoms,
                            treatment = it.treatment,
                            userId = it.userId,
                            imageUrl = it.imageUrl,
                            confidenceScore = it.confidenceScore,
                            cause = it.cause,
                            id = it.id,
                            category = it.category,
                            detectionDate = it.detectionDate
                        )
                    }
                )
            }
        )

    fun detailDetectionResponseToDetailDetection(data: DetailDiseaseResponse): DetailDisease =
        DetailDisease(
            msg = data.msg,
            data = data.data?.map {
                DataItem(
                    symptoms = it?.symptoms,
                    treatment = it?.treatment,
                    userId = it?.userId,
                    imageUrl = it?.imageUrl,
                    confidenceScore = it?.confidenceScore,
                    cause = it?.cause,
                    id = it?.id,
                    category = it?.category,
                    detectionDate = it?.detectionDate
                )
            } ?: emptyList()
        )

    fun userDetectionResponseToUserDetection(data: ListDetectionResponse) : ListDetection = ListDetection(
        msg = data.msg,
        status = data.status,
        data = data.data?.map {
            ItemDetection(
                userId = it?.userId,
                imageUrl = it?.imageUrl,
                category = it?.category,
                detectionDate = it?.detectionDate,
                namePlant = it?.namePlant,
                id = it?.id
            )
        } ?: emptyList()
    )

    fun createPlantResponseToCreatePlant(data: PlantCreateResponse) : PlantCreate = PlantCreate(
        msg = data.msg,
        status = data.status,
        data = data.data?.let {
            Data(
                nama = it.nama,
                userId = it.userId,
                id = it.id
            )
        }
    )

    fun parseErrorMessage(errorBody: String?): String {
        return if (errorBody != null) {

            try {
                val json = Gson().fromJson(errorBody, JsonObject::class.java)
                json.get("detail").asString
            } catch (e: Exception) {
                "Error parsing response"
            }
        } else {
            "Unknown error"
        }
    }


}