package app.weatherapp.network.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import app.weatherapp.domain.model.AstronomyWeather
import app.weatherapp.domain.model.CurrentWeather
import app.weatherapp.domain.model.ForecastWeather
import app.weatherapp.domain.repository.WeatherRepository
import app.weatherapp.network.api.WeatherApiService
import app.weatherapp.network.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class WeatherRepositoryImp(
    private val api: WeatherApiService,
    private val dataStore: DataStore<Preferences>
) : WeatherRepository {
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

    private val CITIES_KEY = stringSetPreferencesKey("favorite_cities")

    override val favouriteCities: Flow<List<String>>
        get() = dataStore.data.map { preferences -> preferences[CITIES_KEY]?.toList() ?: emptyList() }

    override suspend fun saveCity(city: String) {
        dataStore.edit { prefs ->
            val current = prefs[CITIES_KEY] ?: emptySet()
            prefs[CITIES_KEY] = current + city
        }
    }

    override suspend fun removeCity(city: String) {
        dataStore.edit { prefs ->
            val current = prefs[CITIES_KEY] ?: emptySet()
            prefs[CITIES_KEY] = current - city
        }
    }
}
