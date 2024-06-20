package com.example.querizz_app.presentation.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.querizz_app.data.di.AuthInjection
import com.example.querizz_app.presentation.register.RegisterViewModel
import com.example.querizz_app.data.repository.AuthRepository
import com.example.querizz_app.presentation.login.LoginViewModel

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown Auth ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) =
            AuthViewModelFactory(AuthInjection.provideRepositoryAuth(context))
    }
}