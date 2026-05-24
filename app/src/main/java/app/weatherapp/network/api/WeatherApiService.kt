package app.weatherapp.network.api

import android.util.Log
import app.weatherapp.network.networkData.AstronomyWeatherModel
import app.weatherapp.network.networkData.CurrentWeatherModel
import app.weatherapp.network.networkData.ForecastWeatherModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import java.time.LocalDate

internal class WeatherApiService(private val client: HttpClient) {
    private val apiKey = "b0d5aab6ae374c9d9dd153836261505"
    private val baseUrl = "https://api.weatherapi.com/v1"
    private val current = "current.json"
    private val forecast = "forecast.json"
    private val astronomy = "astronomy.json"

    private val dt = LocalDate.now().toString()

    suspend fun getCurrentWeather(city: String): CurrentWeatherModel {
        return client.get("$baseUrl/$current") {
            parameter("key", apiKey)
            parameter("q", city)
            parameter("aqi", "no")
        }.body<CurrentWeatherModel>()
    }

    suspend fun getForecast(city: String): ForecastWeatherModel {
        val response =
            client.get("$baseUrl/$forecast") {
                parameter("key", apiKey)
                parameter("q", city)
                parameter("days", 2)
                parameter("aqi", "no")
                parameter("alerts", "no")
            }
        Log.d("ForecastRaw", response.bodyAsText()) // 👈 add this
        return response.body<ForecastWeatherModel>()
    }

    suspend fun getAstronomy(city: String): AstronomyWeatherModel {
        return client.get("$baseUrl/$astronomy") {
            parameter("key", apiKey)
            parameter("q", city)
            parameter("dt", dt)
        }.body<AstronomyWeatherModel>()
    }
}
