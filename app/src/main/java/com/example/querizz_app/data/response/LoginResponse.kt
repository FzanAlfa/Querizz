package com.example.querizz_app.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
