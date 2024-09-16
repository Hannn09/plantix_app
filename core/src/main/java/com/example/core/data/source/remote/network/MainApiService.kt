package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.DetailDiseaseResponse
import com.example.core.data.source.remote.response.DetectionDiseaseResponse
import com.example.core.data.source.remote.response.ListDetectionResponse
import com.example.core.data.source.remote.response.LoginResponse
import com.example.core.data.source.remote.response.PlantCreateResponse
import com.example.core.data.source.remote.response.RegisterResponse
import com.example.core.data.source.remote.response.UserDetailResponse
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("users/detail")
    suspend fun detailUser(
        @Query("userId") userId: Int
    ) : UserDetailResponse

    @Multipart
    @PUT("users/update{userId}")
    suspend fun updateProfileUser(
        @Path("userId") userId: Int,
        @PartMap profile: Map<String, @JvmSuppressWildcards RequestBody>
    ) : UserDetailResponse

    @Multipart
    @POST("detection/predict")
    suspend fun detectionDisease(
        @PartMap detection: Map<String, @JvmSuppressWildcards RequestBody>
    ) : DetectionDiseaseResponse

    @GET("detection/history/{plantId}")
    suspend fun getDetailDetection(
        @Path("plantId") id: Int
    ) : DetailDiseaseResponse

    @GET("plant/list/{userId}")
    suspend fun getUserDetection(
        @Path("userId") userId: Int
    ) : ListDetectionResponse

    @FormUrlEncoded
    @POST("plant/create")
    suspend fun createPlant(
        @Field("userId") userId: Int,
        @Field("nama") name: String,
    ) : PlantCreateResponse
}