package app.weatherapp.domain.repository

import app.weatherapp.domain.model.AstronomyWeather
import app.weatherapp.domain.model.CurrentWeather
import app.weatherapp.domain.model.ForecastWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): Result<CurrentWeather>
    suspend fun getForecast(city: String): Result<ForecastWeather>
    suspend fun getAstronomy(city: String): Result<AstronomyWeather>
    suspend fun saveCity(city:String)
    suspend fun removeCity(city:String)
    val favouriteCities: Flow<List<String>>


}
