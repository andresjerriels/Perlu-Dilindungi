package com.example.perludilindungi.ui

import com.example.perludilindungi.model.Data
import com.google.gson.annotations.SerializedName


data class CheckInResponse (

    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()

)