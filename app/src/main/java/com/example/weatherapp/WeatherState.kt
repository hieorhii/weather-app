package com.example.weatherapp

data class WeatherState(
    val currentWeather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)