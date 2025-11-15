package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _savedWeather = MutableStateFlow<List<Weather>>(emptyList())
    val savedWeather: StateFlow<List<Weather>> = _savedWeather.asStateFlow()

    private val weatherRepository = WeatherRepository()

    fun fetchWeather(city: String) {
        _weatherState.value = _weatherState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val weather = weatherRepository.getWeather(city)
                _weatherState.value = _weatherState.value.copy(
                    currentWeather = weather,
                    isLoading = false
                )
            } catch (e: Exception) {
                _weatherState.value = _weatherState.value.copy(
                    error = e.message ?: "Неизвестная ошибка",
                    isLoading = false
                )
            }
        }
    }

    fun saveCurrentWeather() {
        val currentWeather = _weatherState.value.currentWeather
        if (currentWeather != null && !_savedWeather.value.contains(currentWeather)) {
            _savedWeather.value = _savedWeather.value + currentWeather
        }
    }
}