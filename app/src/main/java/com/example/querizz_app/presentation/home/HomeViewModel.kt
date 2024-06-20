package com.example.querizz_app.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.querizz_app.data.model.UserModel
import com.example.querizz_app.data.repository.Repository
import com.example.querizz_app.data.response.ApiResponse
import com.example.querizz_app.data.response.SumResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    fun getSummaries(): LiveData<ApiResponse<SumResponse>> = repository.getSummary()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}