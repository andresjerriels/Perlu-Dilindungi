package com.example.perludilindungi.model

import com.google.gson.annotations.SerializedName

data class ProvinceCity(
    @SerializedName("key"   ) var key   : String? = null,
    @SerializedName("value" ) var value : String? = null
)
