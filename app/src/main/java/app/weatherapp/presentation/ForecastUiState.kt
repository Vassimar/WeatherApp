package app.weatherapp.presentation

import app.weatherapp.domain.model.ForecastWeather

sealed class ForecastUiState {
    object ForecastLoading : ForecastUiState()

    data class ForecastSuccess(val weatherCurrent: ForecastWeather) : ForecastUiState()

    data class ForecastError(val message: String) : ForecastUiState()
}
