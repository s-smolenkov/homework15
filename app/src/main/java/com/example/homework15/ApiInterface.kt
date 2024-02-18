package com.example.homework15

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/v1/forecast?latitude=50.4547&longitude=30.5238&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min&wind_speed_unit=ms&timeformat=unixtime&timezone=Africa%2FCairo&forecast_days=3")
    suspend fun getWeatherForKyiv():Response<WeatherResponse>
}