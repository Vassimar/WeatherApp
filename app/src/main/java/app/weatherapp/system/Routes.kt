package app.weatherapp.system

sealed class Routes(val routes: String) {
    object MainWeatherScreen : Routes("weather?city={city}") {
        fun weatherWithCity(city: String?): String {
            return if (city != null) "weather?city=$city" else "weather"
        }
    }

    object SavedWeatherScreen : Routes("saved")
}
