package app.weatherapp.network.networkData

import kotlinx.serialization.Serializable

@Serializable
data class ForecastWeatherModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)
