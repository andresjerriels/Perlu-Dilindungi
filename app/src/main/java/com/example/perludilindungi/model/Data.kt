package com.example.perludilindungi.model

import com.google.gson.annotations.SerializedName


data class Data (

    @SerializedName("userStatus" ) var userStatus : String? = null,
    @SerializedName("reason"     ) var reason     : String? = null

)