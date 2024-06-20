package com.example.querizz_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.querizz_app.data.api.config.ApiConfig
import com.example.querizz_app.data.api.service.ApiService
import com.example.querizz_app.data.model.UserModel
import com.example.querizz_app.data.pref.UserPreference
import com.example.querizz_app.data.response.ApiResponse
import com.example.querizz_app.data.response.LoginResponse
import com.example.querizz_app.data.response.SumResponse
import com.example.querizz_app.data.response.UploadResponse
import com.example.querizz_app.util.reduceFileImage
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class Repository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() = userPreference.logout()

    fun uploadFile(file: File, title: String, subtitle: String): LiveData<ApiResponse<UploadResponse>> = liveData {
        val userSession = userPreference.getSession().firstOrNull()
        if (userSession != null && userSession.token.isNotEmpty()) {
            val token = userSession.token
            val apiService = ApiConfig.getApiService(token)
            emit(ApiResponse.Loading)
            try {
                val imageFile = reduceFileImage(file)

                val titleBody = title.toRequestBody("text/plain".toMediaType())
                val subtitleBody = subtitle.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                val multipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestImageFile
                )
                val response = apiService.uploadFile(multipartBody, titleBody, subtitleBody)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                emit(ApiResponse.Error(errorMessage))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        } else {
            emit(ApiResponse.Error("Error"))
        }
    }

    fun getSummary(): LiveData<ApiResponse<SumResponse>> = liveData {
        val userSession = userPreference.getSession().firstOrNull()
        if (userSession != null && !userSession.token.isNullOrEmpty()) {
            val token = userSession.token
            val apiService = ApiConfig.getApiService(token)
            emit(ApiResponse.Loading)
            try {
                val response = apiService.getSummaries()
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                emit(ApiResponse.Error(errorMessage))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        } else {
            emit(ApiResponse.Error("Error"))
        }
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            Repository(apiService, userPreference)
    }
}