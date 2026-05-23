package app.weatherapp.domain.model

data class CurrentWeather(
    val current: CurrentUi,
    val location: LocationUi,
)
