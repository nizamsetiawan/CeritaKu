package com.bangkit.intermediate.ceritaku.source.network

import com.bangkit.intermediate.ceritaku.source.response.AddStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.AllStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.DetailStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.LoginRequest
import com.bangkit.intermediate.ceritaku.source.response.LoginResponse
import com.bangkit.intermediate.ceritaku.source.response.RegisterRequest
import com.bangkit.intermediate.ceritaku.source.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body login : LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body login : RegisterRequest
    ): RegisterResponse


    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): AllStoryResponse

    @GET("stories/{id}")
    suspend fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

    @GET("stories")
    suspend fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): AllStoryResponse
}