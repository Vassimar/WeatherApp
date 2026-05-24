package app.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.weatherapp.domain.repository.WeatherRepository
import app.weatherapp.system.screens.SavedCities
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedCitiesScreenViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _savedCities = MutableStateFlow<List<SavedCities>>(emptyList())
    val savedCities = _savedCities.asStateFlow()

    init {
        viewModelScope.launch {
            repo.favouriteCities.collect { cities ->
                val result = cities.map { cityName ->
                    async {
                        repo.getCurrentWeather(cityName).getOrNull()?.let { weather ->
                            SavedCities(
                                name = weather.location.name,
                                temp = weather.current.temp_c,
                                img = weather.current.condition.icon,
                                time = weather.location.localtime.substringAfter(" ")

                            )
                        }
                    }
                }.awaitAll().filterNotNull()
                _savedCities.value = result
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    val errorMessage = "Couldn't load weather"
    private val _weather = MutableStateFlow<WeatherUiState>(WeatherUiState.WeatherLoading)
    val weather: StateFlow<WeatherUiState> = _weather
    fun saveCity(city: String) {
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

    fun deleteCity(city: String) {
        viewModelScope.launch {
            repo.removeCity(city)
        }
    }

}
