package app.weatherapp.network.networkData

import kotlinx.serialization.Serializable

@Serializable
data class AstronomyWeatherModel(
    val astronomy: Astronomy,
    val location: Location
)
