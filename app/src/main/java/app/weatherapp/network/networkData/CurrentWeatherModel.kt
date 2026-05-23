package app.weatherapp.network.networkData

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherModel(
    val current: Current,
    val location: Location,
)
