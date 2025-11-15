package com.example.weatherapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(
    onNavigateToSaved: () -> Unit,
    viewModel: WeatherViewModel
) {
    var city by remember { mutableStateOf("") }
    val weatherState by viewModel.weatherState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Город") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (city.isNotBlank()) {
                    coroutineScope.launch {
                        viewModel.fetchWeather(city)
                    }
                }
            },
            enabled = city.isNotBlank()
        ) {
            Text("Получить погоду")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            weatherState.isLoading -> {
                CircularProgressIndicator()
                Text("Загрузка...")
            }
            weatherState.error != null -> {
                Text(
                    text = "Ошибка: ${weatherState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            weatherState.currentWeather != null -> {
                val weather = weatherState.currentWeather!!
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(weather.city, style = MaterialTheme.typography.headlineSmall)
                        Text("${weather.temperature.toInt()}°C", style = MaterialTheme.typography.headlineMedium)
                        Text(weather.description)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Влажность: ${weather.humidity}%")
                            Text("Ветер: ${weather.windSpeed} м/с")
                            Text("Давление: ${weather.pressure} hPa")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.saveCurrentWeather() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(onClick = onNavigateToSaved) {
            Text("Сохраненная погода")
        }
    }
}

@Composable
fun SavedWeatherScreen(
    onNavigateBack: () -> Unit,
    viewModel: WeatherViewModel
) {
    val savedWeather by viewModel.savedWeather.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Сохраненная погода", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
            Button(onClick = onNavigateBack) {
                Text("Назад")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (savedWeather.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет сохраненных данных")
            }
        } else {
            LazyColumn {
                items(savedWeather) { weather ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(weather.city, style = MaterialTheme.typography.bodyLarge)
                            Text("${weather.temperature}°C - ${weather.description}")
                            Text("Влажность: ${weather.humidity}% • Ветер: ${weather.windSpeed} м/с",
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}