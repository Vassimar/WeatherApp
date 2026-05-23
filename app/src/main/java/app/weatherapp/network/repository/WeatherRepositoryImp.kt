package app.weatherapp.network.repository

import android.util.Log
import app.weatherapp.domain.model.AstronomyWeather
import app.weatherapp.domain.model.CurrentWeather
import app.weatherapp.domain.model.ForecastWeather
import app.weatherapp.domain.repository.WeatherRepository
import app.weatherapp.network.api.WeatherApiService
import app.weatherapp.network.mapper.toDomain

internal class WeatherRepositoryImp(private val api: WeatherApiService) : WeatherRepository {
    override suspend fun getCurrentWeather(city: String): Result<CurrentWeather> {
        return try {
            Result.success(api.getCurrentWeather(city).toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun getForecast(city: String): Result<ForecastWeather> {
        return try {
            Result.success(api.getForecast(city).toDomain())
        } catch (e: Exception) {
            Log.e("ForecastRepo", "API call failed: ${e.message}", e)
            Result.failure(e)
        }

    }

    override suspend fun getAstronomy(city: String): Result<AstronomyWeather> {
        return try {
            Result.success(api.getAstronomy(city).toDomain())
        } catch (e: Exception) {
            Log.e("WeatherRepo", "API call failed: ${e.message}", e)
            Result.failure(e)

        }
    }
}

