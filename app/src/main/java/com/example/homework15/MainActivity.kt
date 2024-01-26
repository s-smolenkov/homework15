package com.example.homework15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card: ConstraintLayout = findViewById(R.id.linearLayout)
        val currentDate: TextView = findViewById(R.id.date)
        val currentTime: TextView = findViewById(R.id.time)
        val wmoCode: TextView = findViewById(R.id.wmo_code)
        val currentWeather: TextView = findViewById(R.id.currentWeather)
        val feelsLike: TextView = findViewById(R.id.feels_like)
        val humidity: TextView = findViewById(R.id.humidity)
        val wind: TextView = findViewById(R.id.wind)
        val weatherUpdatedTime: TextView = findViewById(R.id.weatherUpdatedTime)
        val currentWeatherPicture: ImageView = findViewById(R.id.photo)
        val nameOfDay1: TextView = findViewById(R.id.day1)
        val nameOfDay2: TextView = findViewById(R.id.day2)
        val nameOfDay3: TextView = findViewById(R.id.day3)
        val weatherPictureOfDay1: ImageView = findViewById(R.id.photo_day1)
        val weatherPictureOfDay2: ImageView = findViewById(R.id.photo_day2)
        val weatherPictureOfDay3: ImageView = findViewById(R.id.photo_day3)
        val highTempOfDay1: TextView = findViewById(R.id.high_temp_day1)
        val highTempOfDay2: TextView = findViewById(R.id.high_temp_day2)
        val highTempOfDay3: TextView = findViewById(R.id.high_temp_day3)
        val lowTempOfDay1: TextView = findViewById(R.id.low_temp_day1)
        val lowTempOfDay2: TextView = findViewById(R.id.low_temp_day2)
        val lowTempOfDay3: TextView = findViewById(R.id.low_temp_day3)


        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentTimeFormatted = getCurrentTimeFormatted()
                currentTime.text = currentTimeFormatted
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)

        val apiClient = ApiClient.client.create(ApiInterface::class.java)

        apiClient.getWeatherForKyivRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val formattedDateTime = formatUnixTime(it.current.time)
                currentDate.text = formattedDateTime.date
                weatherUpdatedTime.text = "Weather updated time: ${formattedDateTime.time}"
                wmoCode.text = "${getWeatherDescription(it.current.weatherCode)}"
                currentWeather.text = "${roundWeatherData(it.current.temperature2m)}°"
                feelsLike.text =
                    "Feels like : ${roundWeatherData(it.current.apparentTemperature)}°"
                humidity.text = "Humidity: ${roundWeatherData(it.current.relativeHumidity2m)}%"
                wind.text = "Wind: ${roundWeatherData(it.current.windSpeed10m)} m/s"
                val (today, tomorrow, dayAfterTomorrow) = getDaysNames()
                nameOfDay1.text = "$today"
                nameOfDay2.text = "$tomorrow"
                nameOfDay3.text = "$dayAfterTomorrow"
                highTempOfDay1.text = "Hi ${roundWeatherData(it.daily.temperature2mMax[0])}°C"
                highTempOfDay2.text = "Hi ${roundWeatherData(it.daily.temperature2mMax[1])}°C"
                highTempOfDay3.text = "Hi ${roundWeatherData(it.daily.temperature2mMax[2])}°C"
                lowTempOfDay1.text = "Lo ${roundWeatherData(it.daily.temperature2mMin[0])}°C"
                lowTempOfDay2.text = "Lo ${roundWeatherData(it.daily.temperature2mMin[1])}°C"
                lowTempOfDay3.text = "Lo ${roundWeatherData(it.daily.temperature2mMin[2])}°C"
                currentWeatherPicture.setImageResource(getWeatherPicture(it.current.weatherCode))
                weatherPictureOfDay1.setImageResource(getWeatherPicture(it.daily.weatherCode[0]))
                weatherPictureOfDay2.setImageResource(getWeatherPicture(it.daily.weatherCode[1]))
                weatherPictureOfDay3.setImageResource(getWeatherPicture(it.daily.weatherCode[2]))
            }, {
                wmoCode.text = "${it.message}"
                currentDate.text = "${it.message}"
                currentTime.text = "${it.message}"
                currentWeather.text = "${it.message}"
                feelsLike.text = "${it.message}"
                humidity.text = "${it.message}"
                wind.text = "${it.message}"
                nameOfDay1.text = "${it.message}"
                nameOfDay2.text = "${it.message}"
                nameOfDay3.text = "${it.message}"
                highTempOfDay1.text = "${it.message}"
                highTempOfDay2.text = "${it.message}"
                highTempOfDay3.text = "${it.message}"
                lowTempOfDay1.text = "${it.message}"
                lowTempOfDay2.text = "${it.message}"
                lowTempOfDay3.text = "${it.message}"
            })

        card.setOnClickListener {
            apiClient.getWeatherForKyivRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val formattedDateTime = formatUnixTime(it.current.time)
                    currentDate.text = formattedDateTime.date
                    weatherUpdatedTime.text = "Weather updated time: ${formattedDateTime.time}"
                    currentTime.text = getCurrentTimeFormatted()
                    wmoCode.text = "${getWeatherDescription(it.current.weatherCode)}"
                    currentWeather.text = "${roundWeatherData(it.current.temperature2m)}°"
                    feelsLike.text =
                        "Feels like : ${roundWeatherData(it.current.apparentTemperature)}°"
                    humidity.text = "Humidity: ${roundWeatherData(it.current.relativeHumidity2m)}%"
                    wind.text = "Wind: ${roundWeatherData(it.current.windSpeed10m)} m/s"
                    val (today, tomorrow, dayAfterTomorrow) = getDaysNames()
                    nameOfDay1.text = "$today"
                    nameOfDay2.text = "$tomorrow"
                    nameOfDay3.text = "$dayAfterTomorrow"
                    highTempOfDay1.text = "Hi ${roundWeatherData(it.daily.temperature2mMax[0])}°C"
                    highTempOfDay2.text = "Hi ${roundWeatherData(it.daily.temperature2mMax[1])}°C"
                    highTempOfDay3.text = "Hi ${roundWeatherData(it.daily.temperature2mMax[2])}°C"
                    lowTempOfDay1.text = "Lo ${roundWeatherData(it.daily.temperature2mMin[0])}°C"
                    lowTempOfDay2.text = "Lo ${roundWeatherData(it.daily.temperature2mMin[1])}°C"
                    lowTempOfDay3.text = "Lo ${roundWeatherData(it.daily.temperature2mMin[2])}°C"
                    currentWeatherPicture.setImageResource(getWeatherPicture(it.current.weatherCode))
                    weatherPictureOfDay1.setImageResource(getWeatherPicture(it.daily.weatherCode[0]))
                    weatherPictureOfDay2.setImageResource(getWeatherPicture(it.daily.weatherCode[1]))
                    weatherPictureOfDay3.setImageResource(getWeatherPicture(it.daily.weatherCode[2]))
                }, {
                    wmoCode.text = "${it.message}"
                    currentDate.text = "${it.message}"
                    currentTime.text = "${it.message}"
                    currentWeather.text = "${it.message}"
                    feelsLike.text = "${it.message}"
                    humidity.text = "${it.message}"
                    wind.text = "${it.message}"
                    nameOfDay1.text = "${it.message}"
                    nameOfDay2.text = "${it.message}"
                    nameOfDay3.text = "${it.message}"
                    highTempOfDay1.text = "${it.message}"
                    highTempOfDay2.text = "${it.message}"
                    highTempOfDay3.text = "${it.message}"
                    lowTempOfDay1.text = "${it.message}"
                    lowTempOfDay2.text = "${it.message}"
                    lowTempOfDay3.text = "${it.message}"
                })
        }

    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    fun getWeatherDescription(codeStr: String): String {
        val code = codeStr.toIntOrNull() ?: return "Invalid Code"

        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Partly cloudy"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            56, 57 -> "Freezing Drizzle"
            61, 63, 65 -> "Rain"
            66, 67 -> "Freezing Rain"
            71, 73, 75 -> "Snow fall"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers"
            85, 86 -> "Snow showers"
            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with hail"
            else -> "Unknown"
        }
    }

    fun getWeatherPicture(codeStr: String): Int {
        val code = codeStr.toIntOrNull() ?: return R.drawable.invalid_code

        return when (code) {
            0 -> R.drawable.clear_sky
            1, 2, 3 -> R.drawable.partly_cloudy
            45, 48 -> R.drawable.fog
            51, 53, 55 -> R.drawable.drizzle
            56, 57 -> R.drawable.freezing_drizzle
            61, 63, 65 -> R.drawable.rain
            66, 67 -> R.drawable.freezing_rain
            71, 73, 75 -> R.drawable.snow_showers
            77 -> R.drawable.snow_grains
            80, 81, 82 -> R.drawable.rain_showers
            85, 86 -> R.drawable.snow_showers
            95 -> R.drawable.thunderstorm
            96, 99 -> R.drawable.thunderstorm
            else -> R.drawable.unknown
        }
    }

    data class FormattedDateTime(val time: String, val date: String)

    fun formatUnixTime(unixTimeStr: String): FormattedDateTime {
        val unixTime = unixTimeStr.toLong()

        val date = Date(unixTime * 1000)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val timeString = timeFormat.format(date)

        val dateFormat = SimpleDateFormat("MMM-dd-yyyy EEE", Locale.ENGLISH)
        val dateString = dateFormat.format(date)

        return FormattedDateTime(timeString, dateString)
    }

    fun getCurrentTimeFormatted(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("HH : mm", Locale.ENGLISH)
        return dateFormat.format(currentTime)
    }

    fun getDaysNames(): Triple<String, String, String> {
        val dateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        val today = dateFormat.format(calendar.time)
        calendar.add(Calendar.DATE, 1)
        val tomorrow = dateFormat.format(calendar.time)
        calendar.add(Calendar.DATE, 1)
        val dayAfterTomorrow = dateFormat.format(calendar.time)
        return Triple(today, tomorrow, dayAfterTomorrow)
    }

    fun roundWeatherData(weatherDataStr: String): String {
        val weatherData = weatherDataStr.toDoubleOrNull() ?: return "Invalid Data"

        val roundedData = if (weatherData - weatherData.toInt() >= 0.5) {
            kotlin.math.ceil(weatherData)
        } else {
            kotlin.math.floor(weatherData)
        }

        return roundedData.toInt().toString()
    }
}


