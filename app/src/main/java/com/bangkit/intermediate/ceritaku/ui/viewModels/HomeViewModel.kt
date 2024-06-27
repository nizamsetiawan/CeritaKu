package com.bangkit.intermediate.ceritaku.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.bangkit.intermediate.ceritaku.repository.DataRepository
import com.bangkit.intermediate.ceritaku.source.response.DetailStoryResponse
import com.bangkit.intermediate.ceritaku.source.response.ListStoryItem
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import kotlinx.coroutines.launch

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _getAllStory = MutableLiveData<ApiResult<PagingData<ListStoryItem>>>()
    val getAllStory: LiveData<ApiResult<PagingData<ListStoryItem>>> by lazy { _getAllStory }

    private val _getDetailStory = MutableLiveData<ApiResult<DetailStoryResponse>>()
    val getDetailStory: LiveData<ApiResult<DetailStoryResponse>> by lazy { _getDetailStory }

    fun fetchAllStories(token: String) {
        viewModelScope.launch {
            dataRepository.getStory(token).collect {
                _getAllStory.value = it
            }
        }
    }

    fun fetchDetailStories(token: String, id: String) {
        viewModelScope.launch {
            dataRepository.getDetailStory(token, id).collect {
                _getDetailStory.value = it
            }
        }
    }


}