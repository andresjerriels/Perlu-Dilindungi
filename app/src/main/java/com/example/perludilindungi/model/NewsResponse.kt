package com.example.perludilindungi.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("count_total") var countTotal: Int? = null,
    @SerializedName("results") var results: ArrayList<News> = arrayListOf()
)
