package com.example.perludilindungi.ui

import com.google.gson.annotations.SerializedName


data class CheckInRequest (

    @SerializedName("qrCode"    ) var qrCode    : String? = null,
    @SerializedName("latitude"  ) var latitude  : Double? = null,
    @SerializedName("longitude" ) var longitude : Double? = null

)