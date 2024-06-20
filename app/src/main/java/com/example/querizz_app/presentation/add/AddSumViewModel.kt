package com.example.querizz_app.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.querizz_app.data.repository.Repository
import com.example.querizz_app.data.response.UploadResponse
import kotlinx.coroutines.launch
import java.io.File

class AddSumViewModel(private val repository: Repository): ViewModel() {
    private val _response = MutableLiveData<UploadResponse>()
    val response: LiveData<UploadResponse> = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getSession() {
        viewModelScope.launch {
            repository.getSession()
        }
    }

    fun uploadFile(file: File, title: String, subtitle: String) =
        repository.uploadFile(file, title, subtitle)

}