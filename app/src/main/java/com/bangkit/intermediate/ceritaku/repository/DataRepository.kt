package com.bangkit.intermediate.ceritaku.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bangkit.intermediate.ceritaku.source.RemoteDataSource
import com.bangkit.intermediate.ceritaku.source.response.AddStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.AllStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.DetailStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.ListStoryItem
import com.bangkit.intermediate.ceritaku.source.response.LoginRequest
import com.bangkit.intermediate.ceritaku.source.response.LoginResponse
import com.bangkit.intermediate.ceritaku.source.response.RegisterRequest
import com.bangkit.intermediate.ceritaku.source.response.RegisterResponse
import com.bangkit.intermediate.ceritaku.ui.adapter.PagingStory
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import com.bangkit.intermediate.ceritaku.utils.Prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DataRepository(private val remoteDataSource: RemoteDataSource) {

    fun login(loginData: LoginRequest): Flow<ApiResult<LoginResponse>> = flow {
        try {
            emit(ApiResult.Loading)
            val response = remoteDataSource.login(loginData)
            if (response.error == true) {
                emit(ApiResult.Error(response.message.toString()))
            } else {
                val loginResult = response.loginResult
                if (loginResult != null) {
                    Prefs.setLoginPrefs(loginResult)
                }
                emit(ApiResult.Success(response))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown error"))
        }
    }


    fun register(registerData: RegisterRequest): Flow<ApiResult<RegisterResponse>> = flow {
        try {
            emit(ApiResult.Loading)
            val response = remoteDataSource.register(registerData)
            if (response.error == true) {
                emit(ApiResult.Error(response.message.toString()))
            } else {
                emit(ApiResult.Success(response))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ApiResult.Error(e.message.toString()))
        }
    }

    fun getStory(token: String): Flow<ApiResult<PagingData<ListStoryItem>>> = flow {
        try {
            emit(ApiResult.Loading)
            val pagingData = Pager(
                config = PagingConfig(
                    pageSize = 5,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { PagingStory(remoteDataSource, token) }
            ).flow
            pagingData.collect {
                emit(ApiResult.Success(it))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown error"))
        }
    }



    fun getDetailStory(token: String, id: String): Flow<ApiResult<DetailStoryResponse>> = flow {
        try {
            emit(ApiResult.Loading)
            val response = remoteDataSource.getDetailStory("Bearer $token", id)
            if (response.error == true) {
                emit(ApiResult.Error(response.message.toString()))
            } else {
                emit(ApiResult.Success(response))
            }
        }catch (e: Exception) {
            e.printStackTrace()
            emit(ApiResult.Error(e.message.toString()))
        }
    }

    fun upStory(token: String, file: File, description: String): Flow<ApiResult<AddStoryResponse>> = flow {
        try {
            emit(ApiResult.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            val response = remoteDataSource.uploadStories("Bearer $token", multipartBody, requestBody)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown error"))
        }
    }

    fun getStoriesWithLocation(token: String): Flow<ApiResult<AllStoryResponse>> = flow {
        try {
            emit(ApiResult.Loading)
            val response = remoteDataSource.getStoriesWithLocation("Bearer $token")
            if (response.error == true) {
                emit(ApiResult.Error(response.message.toString()))
            } else {
                emit(ApiResult.Success(response))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ApiResult.Error(e.message.toString()))
        }
    }

}