package app.weatherapp.di

import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.weatherapp.domain.repository.WeatherRepository
import app.weatherapp.network.api.WeatherApiService
import app.weatherapp.network.repository.WeatherRepositoryImp
import app.weatherapp.presentation.LocationViewModel
import app.weatherapp.presentation.SavedCitiesScreenViewModel
import app.weatherapp.presentation.SelectedWeatherScreenViewModel
import com.google.android.gms.location.LocationServices
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single {
            HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                        }
                    )
                }
            }
        }
        single {
            PreferenceDataStoreFactory.create(
                produceFile = {
                    androidContext().dataStoreFile("saved_weather_db.preferences_pb")
                }
            )
        }
        single { LocationServices.getFusedLocationProviderClient(androidContext()) }
        singleOf(::WeatherRepositoryImp) { bind<WeatherRepository>() }
        singleOf(::WeatherApiService)
        viewModelOf(::SelectedWeatherScreenViewModel)
        viewModelOf(::SavedCitiesScreenViewModel)
        viewModelOf(::LocationViewModel)
    }
