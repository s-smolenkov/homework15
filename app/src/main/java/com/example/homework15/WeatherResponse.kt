package com.example.homework15

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double,
    @SerializedName("generationtime_ms") var generationtimeMs: String,
    @SerializedName("utc_offset_seconds") var utcOffsetSeconds: String,
    @SerializedName("timezone") var timezone: String,
    @SerializedName("timezone_abbreviation") var timezoneAbbreviation: String,
    @SerializedName("elevation") var elevation: String,
    @SerializedName("current_units") var currentUnits: CurrentUnits,
    @SerializedName("current") var current: Current,
    @SerializedName("daily_units") var dailyUnits: DailyUnits,
    @SerializedName("daily") var daily: Daily
)

data class CurrentUnits(
    @SerializedName("time") var time: String,
    @SerializedName("interval") var interval: String,
    @SerializedName("temperature_2m") var temperature2m: String,
    @SerializedName("relative_humidity_2m") var relativeHumidity2m: String,
    @SerializedName("apparent_temperature") var apparentTemperature: String,
    @SerializedName("weather_code") var weatherCode: String,
    @SerializedName("wind_speed_10m") var windSpeed10m: String
)

data class Current(
    @SerializedName("time") var time: String,
    @SerializedName("interval") var interval: String,
    @SerializedName("temperature_2m") var temperature2m: String,
    @SerializedName("relative_humidity_2m") var relativeHumidity2m: String,
    @SerializedName("apparent_temperature") var apparentTemperature: String,
    @SerializedName("weather_code") var weatherCode: String,
    @SerializedName("wind_speed_10m") var windSpeed10m: String
)

data class DailyUnits(
    @SerializedName("time") var time: String,
    @SerializedName("weather_code") var weatherCode: String,
    @SerializedName("temperature_2m_max") var temperature2mMax: String,
    @SerializedName("temperature_2m_min") var temperature2mMin: String
)

data class Daily(
    @SerializedName("time") var time: ArrayList<String> = arrayListOf(),
    @SerializedName("weather_code") var weatherCode: ArrayList<String> = arrayListOf(),
    @SerializedName("temperature_2m_max") var temperature2mMax: ArrayList<String> = arrayListOf(),
    @SerializedName("temperature_2m_min") var temperature2mMin: ArrayList<String> = arrayListOf()
)



