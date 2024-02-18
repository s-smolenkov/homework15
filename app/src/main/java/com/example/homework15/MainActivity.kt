package com.example.homework15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.example.homework15.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        updateCurrentTimeEverySecond()

        binding.linearLayout.setOnClickListener {
            viewModel.refreshWeather()
        }

    }

    private fun observeViewModel() {
        viewModel.weatherState.observe(this) { state ->
            when (state) {
                is WeatherState.Success -> updateUI(state.data.body())
                is WeatherState.Error -> showError(state.message)
            }
        }
    }

    private fun updateUI(weatherResponse: WeatherResponse?) {
        weatherResponse?.let {
            val formattedDateTime = formatUnixTime(it.current.time)
            binding.date.text = formattedDateTime.date
            "Weather updated time: ${formattedDateTime.time}".also {
                binding.weatherUpdatedTime.text = it
            }
            getWeatherDescription(it.current.weatherCode).also { binding.wmoCode.text = it }
            "${roundWeatherData(it.current.temperature2m)}°".also {
                binding.currentWeather.text = it
            }
            "Feels like : ${roundWeatherData(it.current.apparentTemperature)}°".also {
                binding.feelsLike.text = it
            }
            "Humidity: ${roundWeatherData(it.current.relativeHumidity2m)}%".also {
                binding.humidity.text = it
            }
            "Wind: ${roundWeatherData(it.current.windSpeed10m)} m/s".also { binding.wind.text = it }
            val (today, tomorrow, dayAfterTomorrow) = getDaysNames()
            binding.day1.text = today
            binding.day2.text = tomorrow
            binding.day3.text = dayAfterTomorrow
            "Hi ${roundWeatherData(it.daily.temperature2mMax[0])}°C".also {
                binding.highTempDay1.text = it
            }
            "Hi ${roundWeatherData(it.daily.temperature2mMax[1])}°C".also {
                binding.highTempDay2.text = it
            }
            "Hi ${roundWeatherData(it.daily.temperature2mMax[2])}°C".also {
                binding.highTempDay3.text = it
            }
            "Lo ${roundWeatherData(it.daily.temperature2mMin[0])}°C".also {
                binding.lowTempDay1.text = it
            }
            "Lo ${roundWeatherData(it.daily.temperature2mMin[1])}°C".also {
                binding.lowTempDay2.text = it
            }
            "Lo ${roundWeatherData(it.daily.temperature2mMin[2])}°C".also {
                binding.lowTempDay3.text = it
            }
            binding.photo.setImageResource(getWeatherPicture(it.current.weatherCode))
            binding.photoDay1.setImageResource(getWeatherPicture(it.daily.weatherCode[0]))
            binding.photoDay2.setImageResource(getWeatherPicture(it.daily.weatherCode[1]))
            binding.photoDay3.setImageResource(getWeatherPicture(it.daily.weatherCode[2]))
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateCurrentTimeEverySecond() {
        val handler = Handler(mainLooper)
        val runnable = object : Runnable {
            override fun run() {
                binding.time.text = getCurrentTimeFormatted()
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 10)
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