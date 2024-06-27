package com.bangkit.intermediate.ceritaku.source

import com.bangkit.intermediate.ceritaku.source.network.ApiService
import com.bangkit.intermediate.ceritaku.source.response.LoginRequest
import com.bangkit.intermediate.ceritaku.source.response.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDataSource(private val api : ApiService) {
    suspend fun login(data : LoginRequest) = api.login(data)
    suspend fun register(data : RegisterRequest) = api.register(data)
    suspend fun getStory(token : String, page : Int, size : Int) = api.getStory(token, page, size)
    suspend fun getDetailStory(token : String, id : String) = api.detailStory(token, id)
    suspend fun uploadStories(token : String, fileImage : MultipartBody.Part, description : RequestBody) = api.addStory(token, fileImage, description)
    suspend fun getStoriesWithLocation(token : String) = api.getStoryLocation(token)



}