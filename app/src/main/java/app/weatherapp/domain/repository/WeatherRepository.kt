package app.weatherapp.domain.repository

import app.weatherapp.domain.model.AstronomyWeather
import app.weatherapp.domain.model.CurrentWeather
import app.weatherapp.domain.model.ForecastWeather

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): Result<CurrentWeather>
    suspend fun getForecast(city: String): Result<ForecastWeather>
    suspend fun getAstronomy(city: String): Result<AstronomyWeather>


}
