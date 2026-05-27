package app.weatherapp.ui.theme

import androidx.compose.material3.CardColors
import androidx.compose.ui.graphics.Color
import app.weatherapp.R

fun getWeatherBoxTheme(
    conditionCode: Int,
    isDay: Int
): Int =
    when (conditionCode) {
        1000 -> if (isDay == 1) R.drawable.sunny_screen else R.drawable.night_screen
        1003 -> if (isDay == 1) R.drawable.part_cloudy_screen else R.drawable.cloudy_screen_night
        1006, 1009 -> if (isDay == 1) R.drawable.cloudy_screen else R.drawable.cloudy_screen_night
        1063, 1180, 1183, 1186, 1189, 1192, 1195 -> if (isDay == 1) R.drawable.rainy_screen else R.drawable.rainy_screen_night
        1213, 1216, 1219, 1222, 1225 -> if (isDay == 1) R.drawable.snowy_screen else R.drawable.night_screen
        1087, 1273, 1276 -> if (isDay == 1) R.drawable.thunder_screen else R.drawable.thunder_screen_night
        else -> R.drawable.default_background_two
    }

val frostCard =
    CardColors(
        containerColor = Color.White.copy(alpha = 0.08f),
        contentColor = Color.White,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = Color.Transparent
    )

fun getTextColor(isDay: Int): Color {
    return if (isDay == 1)Color.Black else Color.White
}
