package app.weatherapp.network.networkData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    @SerialName("forecastday") val forecastDay: List<ForecastDay>
)
