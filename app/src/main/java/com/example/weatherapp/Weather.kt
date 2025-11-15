package com.example.weatherapp

data class Weather(
    val city: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String,
    val feelsLike: Double,
    val pressure: Int
)