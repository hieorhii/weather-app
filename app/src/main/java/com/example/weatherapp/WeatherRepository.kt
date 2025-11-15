package com.example.weatherapp

class WeatherRepository {
    private val apiService = WeatherApiService.create()

    private val apiKey = "f9ef3a2b3ade71fcc7198534987c7183"

    suspend fun getWeather(city: String): Weather {
        try {
            val response = apiService.getWeather(city, apiKey)

            return Weather(
                city = response.cityName,
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "Неизвестно",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed,
                icon = getWeatherIcon(response.weather.firstOrNull()?.icon ?: "01d"),
                feelsLike = response.main.feelsLike,
                pressure = response.main.pressure
            )
        } catch (e: Exception) {
            throw Exception("Не удалось получить данные о погоде: ${e.message}")
        }
    }

    private fun getWeatherIcon(iconCode: String): String {
        return when (iconCode) {
            "01d", "01n" -> "Солнечно"
            "02d", "02n" -> "Переменная облачность"
            "03d", "03n" -> "Облачно"
            "04d", "04n" -> "Пасмурно"
            "09d", "09n" -> "Сильный дождь"
            "10d", "10n" -> "Дождь"
            "11d", "11n" -> "Гроза"
            "13d", "13n" -> "Снегопад"
            "50d", "50n" -> "Туман"
            else -> "Неизвестно"
        }
    }
}