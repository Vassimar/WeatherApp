package app.weatherapp.domain.model

data class ForecastWeather(
    val current: CurrentUi,
    val forecast: ForecastUi,
    val location: LocationUi
)
