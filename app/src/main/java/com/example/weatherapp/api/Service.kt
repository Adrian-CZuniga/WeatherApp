package com.example.weatherapp.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("weather?")
    suspend fun getUserLastLocationWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") apiKey:String
    ): Response<JsonResponse>
    

}


private var retrofit = Retrofit.Builder()
    .baseUrl(Utils.BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

var service = retrofit.create(Service::class.java)