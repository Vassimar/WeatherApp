package app.weatherapp.domain.model

data class CurrentUi(
    val chance_of_rain: Int,
    val chance_of_snow: Int,
    val cloud: Int,
    val condition: ConditionUI,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val humidity: Int,
    val is_day: Int,
    val last_updated: String,
    val last_updated_epoch: Int,
    val temp_c: String,
    val temp_f: String,
    val will_it_snow: Int,
    val uv: Double
)
