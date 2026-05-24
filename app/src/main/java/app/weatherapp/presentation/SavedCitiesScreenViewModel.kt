package app.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.weatherapp.domain.repository.WeatherRepository
import app.weatherapp.system.screens.SavedCities
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
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
}


