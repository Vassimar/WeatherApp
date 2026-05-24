package app.weatherapp.presentation

import app.weatherapp.domain.model.CurrentWeather

sealed class WeatherUiState {
    object WeatherLoading : WeatherUiState()

    data class WeatherSuccess(val weatherCurrent: CurrentWeather) : WeatherUiState()

    data class WeatherError(val message: String) : WeatherUiState()
}
