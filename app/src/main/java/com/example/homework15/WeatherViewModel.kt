package com.example.homework15

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState> = _weatherState

    init {
        fetchWeather()
    }

    fun refreshWeather() {
        fetchWeather()
    }

    private fun fetchWeather() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                val response = repo.getWeather()
                if (response.isSuccessful){
                    _weatherState.postValue(WeatherState.Success(response))
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class WeatherState {
    data class Success(val data: Response<WeatherResponse>) : WeatherState()
    data class Error(val message: String) : WeatherState()
}