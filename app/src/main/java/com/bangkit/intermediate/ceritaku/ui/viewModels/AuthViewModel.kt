package com.bangkit.intermediate.ceritaku.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.intermediate.ceritaku.repository.DataRepository
import com.bangkit.intermediate.ceritaku.source.response.LoginRequest
import com.bangkit.intermediate.ceritaku.source.response.LoginResponse
import com.bangkit.intermediate.ceritaku.source.response.RegisterRequest
import com.bangkit.intermediate.ceritaku.source.response.RegisterResponse
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import kotlinx.coroutines.launch

class AuthViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<ApiResult<LoginResponse>>()
    val loginResult: LiveData<ApiResult<LoginResponse>> by lazy { _loginResult }

    private val _registerResult = MutableLiveData<ApiResult<RegisterResponse>>()
    val registerResult: LiveData<ApiResult<RegisterResponse>> by lazy { _registerResult }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            dataRepository.login(LoginRequest(email, password)).collect {
                _loginResult.value = it
            }
        }
    }
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            dataRepository.register(RegisterRequest(name, email, password)).collect {
                _registerResult.value = it
            }
        }
    }


}