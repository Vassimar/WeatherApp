package app.weatherapp.domain.model


data class DayUi(
    val avgHumidity: Int,
    val avgTempC: Double,
    val avgTempF: Double,
    val avgVisKm: Double,
    val avgVisMiles: Double,
    val condition: ConditionUI,
    val dailyChanceOfRain: Int,
    val dailyChanceOfSnow: Int,
    val dailyWillItRain: Int,
    val dailyWillItSnow: Int,
    val maxTempC: Double,
    val maxTempF: Double,
    val maxWindKph: Double,
    val maxWindMph: Double,
    val minTempC: Double,
    val minTempF: Double,
    val totalPrecipIn: Double,
    val totalPrecipMm: Double,
    val totalSnowCm: Double,
    val uv: Double
)
