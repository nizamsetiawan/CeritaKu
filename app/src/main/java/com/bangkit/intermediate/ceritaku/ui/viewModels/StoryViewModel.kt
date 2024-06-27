package com.bangkit.intermediate.ceritaku.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.intermediate.ceritaku.repository.DataRepository
import com.bangkit.intermediate.ceritaku.source.response.AddStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.AllStoryResponse
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _addStoryResult = MutableLiveData<ApiResult<AddStoryResponse>>()
    val addStoryResult: LiveData<ApiResult<AddStoryResponse>> get() = _addStoryResult

    private val _storyLocationResponse = MutableLiveData<ApiResult<AllStoryResponse>>()
    val storyWithLocationResponse: LiveData<ApiResult<AllStoryResponse>> get() = _storyLocationResponse

    fun addNewStory(token: String, file: File, description: String) {
        viewModelScope.launch {
            dataRepository.upStory(token, file, description).collect { response ->
                _addStoryResult.value = response
            }
        }
    }

    fun getStoryWithLocation(token: String) {
        viewModelScope.launch {
            try {
                dataRepository.getStoriesWithLocation(token).collect { response ->
                    _storyLocationResponse.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

}