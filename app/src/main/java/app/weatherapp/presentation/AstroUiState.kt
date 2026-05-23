package app.weatherapp.presentation

import app.weatherapp.domain.model.AstronomyWeather
import app.weatherapp.network.networkData.AstronomyWeatherModel

sealed class AstroUiState {
    object AstroLoading : AstroUiState()

    data class AstroSuccess(val astro: AstronomyWeather) : AstroUiState()

    data class AstroError(val message: String) : AstroUiState()
}
