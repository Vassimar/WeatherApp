package app.weatherapp.domain.model

import app.weatherapp.network.networkData.Astronomy
import app.weatherapp.network.networkData.Location

data class AstronomyWeather(
    val astronomy: AstronomyUi,
    val location: LocationUi
)
