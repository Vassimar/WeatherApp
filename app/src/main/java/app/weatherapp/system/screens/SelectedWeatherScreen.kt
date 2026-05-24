package app.weatherapp.system.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.weatherapp.R
import app.weatherapp.domain.model.HourUi
import app.weatherapp.presentation.AstroUiState
import app.weatherapp.presentation.ForecastUiState
import app.weatherapp.presentation.SelectedWeatherScreenViewModel
import app.weatherapp.presentation.WeatherUiState
import app.weatherapp.ui.theme.Typography
import app.weatherapp.ui.theme.WeatherCardTheme
import app.weatherapp.ui.theme.getWeatherTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.koin.androidx.compose.koinViewModel
import app.weatherapp.system.components.displayDay
import app.weatherapp.system.components.displayTime
import app.weatherapp.system.components.fakeAstrology
import app.weatherapp.system.components.fakeForecastWeather
import app.weatherapp.system.components.fakeHourUi
import app.weatherapp.system.components.fakeWeather
import app.weatherapp.ui.theme.getWeatherBoxTheme

@Composable
internal fun SelectedWeatherScreen(
    viewModel: SelectedWeatherScreenViewModel = koinViewModel(),
    text: String?
) {
    val pullRefreshState = rememberPullToRefreshState()
    val weather by viewModel.weather.collectAsState()
    val city = text ?: "London"
    val astrology by viewModel.astrology.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val forecast by viewModel.forecast.collectAsState()
    val forecastShown by viewModel.forecastHoursShown.collectAsState()
    val backgroundRes = when (val state = weather) {
        is WeatherUiState.WeatherSuccess -> getWeatherBoxTheme(
            conditionCode = state.weatherCurrent.current.condition.code,
            isDay = state.weatherCurrent.current.is_day
        )
        else -> R.drawable.default_screen
    }

    LaunchedEffect(city) {
        viewModel.fetchCurrentWeather(city)
        viewModel.fetchAstrology(city)
        viewModel.fetchForecast(city)

    }
    LaunchedEffect(weather) {
        if (weather is WeatherUiState.WeatherSuccess) {
            viewModel.saveCity(city)
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.fetchCurrentWeather(city)
            viewModel.fetchAstrology(city)
            viewModel.fetchForecast(city)
        },
        state = pullRefreshState
    ) {
        SelectedWeatherScreenContent(
            weatherState = weather,
            astrologyState = astrology,
            forecastState = forecast,
            forecastShown = forecastShown,
            backgroundRes = backgroundRes
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun SelectedWeatherScreenContent(
    astrologyState: AstroUiState,
    weatherState: WeatherUiState,
    forecastState: ForecastUiState,
    forecastShown: List<HourUi>,
    backgroundRes:Int
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenHeight: Dp = maxHeight
        Image(
            painter = painterResource(backgroundRes),
            contentDescription = "background_image",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeight)
                .verticalScroll(rememberScrollState())
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherContent(
                weatherState,
                astrologyState,
                forecastState = forecastState,
                forecastShown,

                )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeatherContent(
    weatherState: WeatherUiState,
    astrologyState: AstroUiState,
    forecastState: ForecastUiState,
    forecastShown: List<HourUi>,

    ) {
    val padding = 8.dp
    when (weatherState) {
        is WeatherUiState.WeatherLoading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

        is WeatherUiState.WeatherSuccess -> {

            val myTheme = getWeatherTheme(
                conditionCode = weatherState.weatherCurrent.current.condition.code,
                isDay = weatherState.weatherCurrent.current.is_day
            )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),

                    ) {
                    WeatherMainCard(weatherState, padding, myTheme)
                    WeatherDetailCard(weatherState, myTheme, padding)
                    ForecastContent(forecastState, padding, theme = myTheme, forecastShown)
                    AstrologyContent(astrologyState, theme = myTheme, padding)
                }
        }

        is WeatherUiState.WeatherError -> Text(weatherState.message)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeatherMainCard(
    weather: WeatherUiState.WeatherSuccess,
    padding: Dp,
    theme: WeatherCardTheme
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = theme.contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(12.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            GlideImage(
                modifier = Modifier
                    .size(64.dp)
                    .fillMaxWidth(),
                model = weather.weatherCurrent.current.condition.icon,
                contentDescription = "weather_icon",
                alignment = Alignment.Center
            )
            Text(
                weather.weatherCurrent.location.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                style = Typography.displayLarge,
                textAlign = TextAlign.Center
            )
            Text(
                weather.weatherCurrent.location.country,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                style = Typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Text(
                weather.weatherCurrent.current.temp_c,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                style = Typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                weather.weatherCurrent.location.localtime.substringAfter(" "),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                style = Typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun WeatherDetailCard(
    weather: WeatherUiState.WeatherSuccess,
    theme: WeatherCardTheme,
    padding: Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 40.dp),
        colors = CardColors(
            containerColor = theme.detailCardColor.copy(alpha = 0.1f),
            contentColor = theme.contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.DarkGray
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Feels like:",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium
                )
                Text(
                    "${weather.weatherCurrent.current.feelslike_c}C",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium
                )
                Text(
                    "${weather.weatherCurrent.current.feelslike_f}F",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium
                )
            }
            Column {
                Text(
                    "Humidity: ${weather.weatherCurrent.current.humidity}%",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium
                )
                Text(
                    "Chance of rain: ${weather.weatherCurrent.current.chance_of_rain}%",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium
                )
                Text(
                    "UV Index: ${weather.weatherCurrent.current.uv}",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium
                )
            }
        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ForecastContent(
    forecastState: ForecastUiState,
    padding: Dp,
    theme: WeatherCardTheme,
    forecastShown: List<HourUi>
) {
    when (forecastState) {
        is ForecastUiState.ForecastLoading -> CircularProgressIndicator()
        is ForecastUiState.ForecastSuccess -> {
            val currentHour = forecastState.weatherCurrent.location.localtime
                .substringAfter(" ").substringBefore(":").toIntOrNull() ?: 0

            Card(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 40.dp)
                    .fillMaxWidth(),
                colors = CardColors(
                    containerColor = theme.forecastCardColor,
                    contentColor = theme.contentColor,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.DarkGray,
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    forecastShown.forEach { hour ->
                        Column(
                            modifier = Modifier.padding(padding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            GlideImage(
                                hour.condition.icon,
                                "weather_icon"
                            )
                            Text(
                                hour.displayTime(currentHour)
                            )
                            Text(hour.tempC)
                            Text(hour.displayDay())

                        }


                    }
                }
            }
        }

        is ForecastUiState.ForecastError -> Text(forecastState.message)
    }
}

@Composable
private fun AstrologyContent(astrologyState: AstroUiState, theme: WeatherCardTheme, padding: Dp) {
    when (astrologyState) {
        is AstroUiState.AstroLoading -> CircularProgressIndicator()
        is AstroUiState.AstroSuccess -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp),
                colors = CardColors(
                    containerColor = theme.astroCardColor.copy(alpha = 0.3f),
                    contentColor = theme.contentColor,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.DarkGray,
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sunset: ${astrologyState.astro.astronomy.astro.sunset}")
                    Text("Sunrise: ${astrologyState.astro.astronomy.astro.sunrise}")
                    Text("Moon looks like: ${astrologyState.astro.astronomy.astro.moon_phase}")
                }
            }
        }

        is AstroUiState.AstroError -> Text(astrologyState.message)
    }
}

@Preview
@Composable
private fun PreviewContent() {
    SelectedWeatherScreenContent(
        weatherState = WeatherUiState.WeatherSuccess(fakeWeather),
        astrologyState = AstroUiState.AstroSuccess(fakeAstrology),
        forecastState = ForecastUiState.ForecastSuccess(fakeForecastWeather),
        forecastShown = fakeHourUi,
        backgroundRes = R.drawable.default_screen
    )
}

