package com.example.querizz_app.data.response

sealed class ApiResponse<out R> private constructor() {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val error:String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}