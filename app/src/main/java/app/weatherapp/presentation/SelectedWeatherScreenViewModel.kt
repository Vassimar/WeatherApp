package app.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.weatherapp.domain.model.ForecastWeather
import app.weatherapp.domain.model.HourUi
import app.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectedWeatherScreenViewModel(
    private val repo: WeatherRepository
) : ViewModel() {
    private val errorMessage = "Couldn't load weather"
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    private val _weather = MutableStateFlow<WeatherUiState>(WeatherUiState.WeatherLoading)
    val weather = _weather.asStateFlow()
    private val _astro = MutableStateFlow<AstroUiState>(AstroUiState.AstroLoading)
    val astrology = _astro.asStateFlow()
    private val _forecast = MutableStateFlow<ForecastUiState>(ForecastUiState.ForecastLoading)
    val forecast = _forecast.asStateFlow()
    private val _forecastHoursShown = MutableStateFlow<List<HourUi>>(emptyList())
    val forecastHoursShown = _forecastHoursShown.asStateFlow()
    private var isLoading = false

    fun loadCity(city: String) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val weatherDeferred = async { repo.getCurrentWeather(city) }
                val astroDeferred = async { repo.getAstronomy(city) }
                val forecastDeferred = async { repo.getForecast(city) }
                val weatherResult = weatherDeferred.await()
                val astroResult = astroDeferred.await()
                val forecastResult = forecastDeferred.await()
                weatherResult
                    .onSuccess { _weather.value = WeatherUiState.WeatherSuccess(it) }
                    .onFailure { _weather.value = WeatherUiState.WeatherError(errorMessage) }
                astroResult
                    .onSuccess { _astro.value = AstroUiState.AstroSuccess(it) }
                    .onFailure { _astro.value = AstroUiState.AstroError(errorMessage) }
                forecastResult
                    .onSuccess {
                        _forecast.value = ForecastUiState.ForecastSuccess(it)
                        _forecastHoursShown.value =
                            computeForecastHours(it)
                    }
                    .onFailure {
                        _forecast.value = ForecastUiState.ForecastError(errorMessage)
                    }

                repo.saveCity(city)
            } catch (e: Exception) {
                Log.e("WeatherVM", "loadCity failed", e)
            } finally {
                _isRefreshing.value = false
                isLoading = false
            }
        }
    }

    private fun computeForecastHours(forecastWeather: ForecastWeather): List<HourUi> {
        val localtime = forecastWeather.location.localtime
        val today = localtime.substringBefore(" ")
        val currentHour =
            localtime
                .substringAfter(" ")
                .substringBefore(":")
                .toIntOrNull() ?: 0
        return forecastWeather.forecast.forecastDays
            .flatMap { it.hour }
            .filter { hour ->
                val (date, time) = hour.time.split(" ", limit = 2)
                val hourOfDay = time.substringBefore(":").toIntOrNull() ?: return@filter false

                if (date == today) hourOfDay >= currentHour else true
            }
    }
}
