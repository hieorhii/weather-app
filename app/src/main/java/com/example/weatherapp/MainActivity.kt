package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.WeatherappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherappTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }

    val viewModel: WeatherViewModel = viewModel()

    when (currentScreen) {
        Screen.Main -> WeatherScreen(
            onNavigateToSaved = { currentScreen = Screen.Saved },
            viewModel = viewModel
        )
        Screen.Saved -> SavedWeatherScreen(
            onNavigateBack = { currentScreen = Screen.Main },
            viewModel = viewModel
        )
    }
}

sealed class Screen {
    object Main : Screen()
    object Saved : Screen()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherappTheme {
        WeatherApp()
    }
}