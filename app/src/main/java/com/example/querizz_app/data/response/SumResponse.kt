package com.example.querizz_app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SumResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = emptyList(),

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataItem(
	@SerializedName("createdAt") val createdAt: String? = null,
	@SerializedName("subtitle") val subtitle: String? = null,
	@SerializedName("id") val id: String? = null,
	@SerializedName("title") val title: String? = null,
	@SerializedName("results") val results: List<ResultsItem?>? = null,
	@SerializedName("username") val username: String? = null
) : Parcelable

@Parcelize
data class ResultsItem(
	@SerializedName("suggestion") val suggestion: String? = null,
	@SerializedName("model") val model: String? = null,
	@SerializedName("label") val label: String? = null
) : Parcelable
