package com.example.querizz_app.data.di

import android.content.Context
import com.example.querizz_app.data.api.config.ApiConfig
import com.example.querizz_app.data.pref.UserPreference
import com.example.querizz_app.data.repository.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context)
        val user = runBlocking {
            pref.getSession().first()
        }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository(apiService, pref)
    }
}