package com.example.perludilindungi.api

import com.example.perludilindungi.model.*
import com.example.perludilindungi.ui.CheckInRequest
import com.example.perludilindungi.ui.CheckInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api/get-news")
    fun getNews(): Call<NewsResponse>

    @GET("api/get-faskes-vaksinasi")
    fun getFaskesVaksinasi(
        @Query("province") province: String,
        @Query("city") city: String
    ): Call<FaskesResponse>

    @GET("api/get-province")
    fun getProvince(): Call<ProvinceCityResponse>

    @GET("api/get-city")
    fun getCity(
        @Query("start_id") startId: String
    ): Call<ProvinceCityResponse>

    @POST("check-in")
    fun checkIn(@Body checkInData: CheckInRequest): Call<CheckInResponse>
}