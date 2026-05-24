package app.weatherapp.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.weatherapp.domain.model.ForecastUi
import app.weatherapp.domain.model.HourUi
import app.weatherapp.domain.repository.WeatherRepository
import app.weatherapp.network.api.WeatherApiService
import app.weatherapp.network.networkData.Hour
import app.weatherapp.network.repository.WeatherRepositoryImp


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectedWeatherScreenViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    val errorMessage = "Couldn't load weather"
    private val _weather = MutableStateFlow<WeatherUiState>(WeatherUiState.WeatherLoading)
    val weather: StateFlow<WeatherUiState> = _weather
    fun saveCity(city: String){
        viewModelScope.launch {
            repo.saveCity(city)
        }
    }


    fun fetchCurrentWeather(city: String) {
        viewModelScope.launch {
            _isRefreshing.value = true
            _weather.value = WeatherUiState.WeatherLoading
            repo.getCurrentWeather(city)
                .onSuccess { currentWeather ->
                    _weather.value = WeatherUiState.WeatherSuccess(currentWeather)
                }
                .onFailure { exception ->
                    Log.e("WeatherVM", "Failed to load weather: ${exception.message}")
                    _weather.value = WeatherUiState.WeatherError(errorMessage)

                }
            _isRefreshing.value = false
        }
    }

    private val _astro = MutableStateFlow<AstroUiState>(AstroUiState.AstroLoading)
    val astrology: StateFlow<AstroUiState> = _astro

    fun fetchAstrology(city: String) {
        viewModelScope.launch {
            _astro.value = AstroUiState.AstroLoading
            repo.getAstronomy(city)
                .onSuccess { astronomyWeather ->
                    _astro.value = AstroUiState.AstroSuccess(astronomyWeather)
                }
                .onFailure {
                    _astro.value = AstroUiState.AstroError(errorMessage)
                }
        }
    }

    private val _forecast = MutableStateFlow<ForecastUiState>(ForecastUiState.ForecastLoading)
    val forecast = _forecast.asStateFlow()
    private val _forecastHoursShown = MutableStateFlow<List<HourUi>>(emptyList())
    val forecastHoursShown = _forecastHoursShown.asStateFlow()
    fun fetchForecast(city: String) {
        viewModelScope.launch {
            _forecast.value = ForecastUiState.ForecastLoading
            repo.getForecast(city)
                .onSuccess { forecastWeather ->
                    val successState = ForecastUiState.ForecastSuccess(forecastWeather)
                    _forecast.value = ForecastUiState.ForecastSuccess(forecastWeather)
                    _forecastHoursShown.value = computeForecastHours(successState)
                }
                .onFailure { exception ->
                    Log.e("ForecastFail", "${exception.message}")
                    _forecast.value = ForecastUiState.ForecastError(errorMessage)
                }
        }
    }

    private fun computeForecastHours(forecastWeather: ForecastUiState.ForecastSuccess): List<HourUi> {
        val localtime = forecastWeather.weatherCurrent.location.localtime
        val today = localtime.substringBefore(" ")
        val currentHour = localtime.substringAfter(" ").substringBefore(":").toIntOrNull() ?: 0

        return forecastWeather.weatherCurrent.forecast.forecastDays
            .flatMap { it.hour }
            .filter { hour ->
                val (hourDate, hourTime) = hour.time.split(" ", limit = 2)
                val hourOfDay = hourTime.substringBefore(":").toIntOrNull() ?: return@filter false
                if (hourDate == today) hourOfDay >= currentHour else true
            }

    }


}



