package com.example.querizz_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.querizz_app.data.api.service.AuthApiService
import com.example.querizz_app.data.response.RegisterResponse
import com.example.querizz_app.data.model.UserModel
import com.example.querizz_app.data.pref.UserPreference
import com.example.querizz_app.data.response.ApiResponse
import com.example.querizz_app.data.response.LoginResponse
import com.google.gson.Gson
import retrofit2.HttpException

class AuthRepository(
    private val authApiService: AuthApiService,
    private val userPreference: UserPreference
) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return authApiService.register(name, email, password)
    }

    fun login(email: String, password: String) : LiveData<ApiResponse<LoginResponse>> = liveData {
        emit(ApiResponse.Loading)
        try {
            val response = authApiService.login(email, password)
            emit(ApiResponse.Success(response))
        } catch (e : HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(ApiResponse.Error(errorMessage))
        } catch (e : Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    companion object {
        fun getInstance(apiServiceAuth: AuthApiService, userPreference: UserPreference) =
            AuthRepository(apiServiceAuth, userPreference)
    }
}