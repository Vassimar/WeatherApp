package app.weatherapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.coroutines.resume

class LocationViewModel(
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {
    private val _city = MutableStateFlow<String?>(null)
    val city: StateFlow<String?> = _city

    fun fetchCity(context: Context) {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("LocationVM", "Location permission not granted")
            return
        }

        viewModelScope.launch {
            try {
                val location =
                    fusedLocationClient.lastLocation.await()
                        ?: fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            null
                        ).await()
                if (location == null) {
                    Log.e("LocationVM", "Location is null")
                    _city.value = null
                    return@launch
                }
                Log.d(
                    "LocationVM",
                    "Lat: ${location.latitude}, Lng: ${location.longitude}"
                )
                val cityName =
                    getCityFromGeocoder(
                        context,
                        location.latitude,
                        location.longitude
                    )
                        ?: getCityFromNominatim(
                            location.latitude,
                            location.longitude
                        )
                Log.d("LocationVM", "Resolved city: $cityName")
                _city.value = cityName
            } catch (e: Exception) {
                Log.e("LocationVM", "fetchCity failed", e)
                _city.value = null
            }
        }
    }

    private suspend fun getCityFromGeocoder(
        context: Context,
        lat: Double,
        lng: Double
    ): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(
                        lat,
                        lng,
                        1
                    ) { addresses ->
                        val city =
                            addresses
                                .firstOrNull()
                                ?.locality
                        Log.d("LocationVM", "Geocoder city: $city")
                        continuation.resume(city)
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                val city =
                    addresses
                        ?.firstOrNull()
                        ?.locality
                Log.d("LocationVM", "Geocoder city: $city")
                city
            }
        } catch (e: Exception) {
            Log.e("LocationVM", "Geocoder failed", e)
            null
        }
    }

    private suspend fun getCityFromNominatim(
        lat: Double,
        lng: Double
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url =
                    URL(
                        "https://nominatim.openstreetmap.org/reverse" +
                            "?lat=$lat&lon=$lng&format=jsonv2"
                    )
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty(
                    "User-Agent",
                    "WeatherApp/1.0"
                )
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                val responseCode = connection.responseCode
                Log.d(
                    "LocationVM",
                    "Nominatim response code: $responseCode"
                )
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(
                        "LocationVM",
                        "HTTP error: $responseCode"
                    )
                    return@withContext null
                }
                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use { it.readText() }
                Log.d(
                    "LocationVM",
                    "Nominatim response: $response"
                )
                val json = JSONObject(response)
                val address =
                    json.optJSONObject("address")
                        ?: return@withContext null
                val city =
                    address.optString("city")
                        .ifEmpty { address.optString("town") }
                        .ifEmpty { address.optString("village") }
                        .ifEmpty { address.optString("municipality") }
                        .ifEmpty { null }
                Log.d("LocationVM", "Nominatim city: $city")
                city
            } catch (e: Exception) {
                Log.e(
                    "LocationVM",
                    "Nominatim failed",
                    e
                )
                null
            }
        }
    }
}
