package com.example.querizz_app.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.querizz_app.data.repository.AuthRepository
import com.example.querizz_app.data.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: AuthRepository): ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    sealed class RegisterResult {
        data class Success(val response: String): RegisterResult()
        data class Error(val error: String): RegisterResult()
        object Loading: RegisterResult()
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _registerResult.value = RegisterResult.Loading
            try {
                val response = repository.register(name, email, password)
                _registerResult.value = RegisterResult.Success(response.message ?: "User created")
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Error(e.message ?: "Failed to create user")
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _registerResult.value = errorMessage?.let {
                    RegisterResult.Error(it)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}