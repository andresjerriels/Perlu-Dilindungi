package com.example.perludilindungi.model

import com.google.gson.annotations.SerializedName

data class ProvinceCityResponse (
    @SerializedName("curr_val" ) var currVal : String?            = null,
    @SerializedName("message"  ) var message : String?            = null,
    @SerializedName("results"  ) var results : ArrayList<ProvinceCity> = arrayListOf()
)