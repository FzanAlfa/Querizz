package com.example.querizz_app.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.querizz_app.data.model.UserModel
import com.example.querizz_app.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository): ViewModel() {

    private val _saveSessionStatus = MutableLiveData<Boolean>()
    val saveSessionStatus: LiveData<Boolean> = _saveSessionStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            try {
                _saveSessionStatus.postValue(true)
                repository.saveSession(user)
            } catch (e: Exception) {
                _saveSessionStatus.postValue(false)
            }
        }
    }

}