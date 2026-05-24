package app.weatherapp.ui.theme

import androidx.compose.ui.graphics.Color
import app.weatherapp.R

data class WeatherCardTheme(
    val containerColor: Color,
    val contentColor: Color,
    val detailCardColor: Color,
    val astroCardColor: Color,
    val forecastCardColor: Color
)

fun getWeatherBoxTheme(conditionCode: Int, isDay: Int): Int=
    when (conditionCode) {
        1000 -> if (isDay == 1) R.drawable.sunny_screen
        else R.drawable.night_screen
        1003 -> R.drawable.part_cloudy_screen
        1006, 1009 -> R.drawable.cloudy_screen
        1063, 1180, 1183, 1186, 1189, 1192, 1195 -> R.drawable.rainy_screen
        1213, 1216, 1219, 1222, 1225 -> R.drawable.snowy_screen
        1087, 1273, 1276 -> R.drawable.thunder_screen
        else -> R.drawable.default_screen
    }

fun getWeatherTheme(conditionCode: Int, isDay: Int): WeatherCardTheme =
    when (conditionCode) {
        1000 -> if (isDay == 1) WeatherCardTheme(
            containerColor = Color(0x86D6EA08),
            contentColor = Color(0xFF0D2D1A),
            detailCardColor = Color(0xFF7ECBA8),
            astroCardColor = Color(0xFF4FA87A),
            forecastCardColor = Color(0x4D5BBFCF)
        ) else WeatherCardTheme(
            containerColor = Color(0xFF1A237E),
            contentColor = Color(0xFF000000),
            detailCardColor = Color(0xFF0D2B5E),
            astroCardColor = Color(0xFF071A3E),
            forecastCardColor = Color(0x230A1F6E)
        )

        1003 -> WeatherCardTheme(
            containerColor = Color(0xFF78909C),
            contentColor = Color(0xFF000000),
            detailCardColor = Color(0xFF4A6978),
            astroCardColor = Color(0xFF2C4A58),
            forecastCardColor = Color(0x4D3A5A68)
        )

        1006, 1009 -> WeatherCardTheme(
            containerColor = Color(0xFF546E7A),
            contentColor = Color(0xFFF7F9FA),
            detailCardColor = Color(0xFF3A515B),
            astroCardColor = Color(0xFF253740),
            forecastCardColor = Color(0x4D2E4450)
        )

        1063, 1180, 1183, 1186, 1189, 1192, 1195 -> WeatherCardTheme(
            containerColor = Color(0xFF1565C0),
            contentColor = Color(0xFFE8EAF6),
            detailCardColor = Color(0xFF0A3570),
            astroCardColor = Color(0xFF061F47),
            forecastCardColor = Color(0x4D082A5E)
        )

        1213, 1216, 1219, 1222, 1225 -> WeatherCardTheme(
            containerColor = Color(0xFFE3F2FD),
            contentColor = Color(0xFF1A1A4E),
            detailCardColor = Color(0xFFC5CAE9),
            astroCardColor = Color(0xFF9FA8DA),
            forecastCardColor = Color(0x4DB0BEF0)
        )

        1087, 1273, 1276 -> WeatherCardTheme(
            containerColor = Color(0xFF37474F),
            contentColor = Color(0xFFFFB300),
            detailCardColor = Color(0xFF263238),
            astroCardColor = Color(0xFF161E22),
            forecastCardColor = Color(0x4D1C262B)
        )

        else -> WeatherCardTheme(
            containerColor = Color(0xFF607D8B),
            contentColor = Color.White,
            detailCardColor = Color(0xFF78909C),
            astroCardColor = Color(0xFF90A4AE),
            forecastCardColor = Color(0xFF000030)
        )
    }
