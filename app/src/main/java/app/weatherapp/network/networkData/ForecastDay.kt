package app.weatherapp.network.networkData

import kotlinx.serialization.Serializable

@Serializable
class ForecastDay(
    val astro: Astro? = null,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)
