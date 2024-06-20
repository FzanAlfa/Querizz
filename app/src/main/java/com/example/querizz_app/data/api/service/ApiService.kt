package com.example.querizz_app.data.api.service

import com.example.querizz_app.data.response.SumResponse
import com.example.querizz_app.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("subtitle") subtitle: RequestBody
    ): UploadResponse

    @GET("getHistories")
    suspend fun getSummaries(): SumResponse
}