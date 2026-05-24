package app.weatherapp.domain.model

data class SavedCities(
    val cityKey: String,
    val code: Int,
    val name: String,
    val temp: String,
    val img: String,
    val time: String,
    val isDay: Int
)
