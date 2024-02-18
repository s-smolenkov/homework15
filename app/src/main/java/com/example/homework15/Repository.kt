package com.example.homework15

import retrofit2.Response

class Repository (val apiClient: ApiClient){
    suspend fun getWeather() : Response<WeatherResponse> {
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        return apiInterface.getWeatherForKyiv()
    }
}