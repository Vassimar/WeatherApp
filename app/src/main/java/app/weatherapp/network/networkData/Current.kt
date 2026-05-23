package app.weatherapp.network.networkData

import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val chance_of_rain: Int,
    val chance_of_snow: Int,
    val cloud: Int,
    val condition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val humidity: Int,
    val is_day: Int,
    val last_updated: String,
    val last_updated_epoch: Int,
    val temp_c: Double,
    val temp_f: Double,
    val will_it_snow: Int,
    val uv: Double
)
