package app.weatherapp.system.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
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
import app.weatherapp.system.components.displayDay
import app.weatherapp.system.components.displayTime
import app.weatherapp.system.components.fakeAstrology
import app.weatherapp.system.components.fakeForecastWeather
import app.weatherapp.system.components.fakeHourUi
import app.weatherapp.system.components.fakeWeather
import app.weatherapp.ui.theme.Typography
import app.weatherapp.ui.theme.frostCard
import app.weatherapp.ui.theme.getTextColor
import app.weatherapp.ui.theme.getWeatherBoxTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SelectedWeatherScreen(
    viewModel: SelectedWeatherScreenViewModel = koinViewModel(),
    text: String?
) {
    val pullRefreshState = rememberPullToRefreshState()
    val weather by viewModel.weather.collectAsState()
    val astrology by viewModel.astrology.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val forecast by viewModel.forecast.collectAsState()
    val forecastShown by viewModel.forecastHoursShown.collectAsState()
    val backgroundRes =
        when (val state = weather) {
            is WeatherUiState.WeatherSuccess ->
                getWeatherBoxTheme(
                    conditionCode = state.weatherCurrent.current.condition.code,
                    isDay = state.weatherCurrent.current.is_day
                )

            else -> null
        }
    val textColor =
        when (val state = weather) {
            is WeatherUiState.WeatherSuccess ->
                getTextColor(isDay = state.weatherCurrent.current.is_day)

            else -> Color.White
        }
    if (text == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    LaunchedEffect(text) {
        text.let {
            viewModel.loadCity(it)
        }
    }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            text.let { viewModel.loadCity(it) }
        },
        state = pullRefreshState
    ) {
        SelectedWeatherScreenContent(
            weatherState = weather,
            astrologyState = astrology,
            forecastState = forecast,
            forecastShown = forecastShown,
            backgroundRes = backgroundRes,
            textColor = textColor,
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
    backgroundRes: Int?,
    textColor: Color,
) {
    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxSize()
    ) {
        backgroundRes?.let { bgRes ->
            Image(
                painter = painterResource(bgRes),
                contentDescription = stringResource(R.string.default_background),
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherContent(
                weatherState,
                astrologyState,
                forecastState = forecastState,
                forecastShown,
                textColor
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
    textColor: Color
) {
    val padding = 8.dp
    when (weatherState) {
        is WeatherUiState.WeatherLoading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

        is WeatherUiState.WeatherSuccess -> {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding),
            ) {
                WeatherMainCard(weatherState, padding, textColor)
                WeatherDetailCard(weatherState, padding, textColor)
                ForecastContent(forecastState, padding, forecastShown, textColor)
                AstrologyContent(astrologyState, padding, textColor)
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
    textColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        frostCard,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                modifier =
                    Modifier
                        .size(64.dp),
                model = weather.weatherCurrent.current.condition.icon,
                contentDescription = stringResource(R.string.weather_Icon),
                alignment = Alignment.Center
            )
            Text(
                weather.weatherCurrent.location.name,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding),
                style = Typography.displayLarge,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                weather.weatherCurrent.location.country,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding),
                style = Typography.displaySmall,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                weather.weatherCurrent.current.temp_c,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding),
                style = Typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                weather.weatherCurrent.location.localtime.substringAfter(" "),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding),
                style = Typography.titleMedium,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}

@Composable
private fun WeatherDetailCard(
    weather: WeatherUiState.WeatherSuccess,
    padding: Dp,
    textColor: Color
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 40.dp),
        colors =
        frostCard,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    stringResource(R.string.feels_like),
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium,
                    color = textColor
                )
                Text(
                    "${weather.weatherCurrent.current.feelslike_c}C",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium,
                    color = textColor
                )
                Text(
                    "${weather.weatherCurrent.current.feelslike_f}F",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium,
                    color = textColor
                )
            }
            Column {
                Text(
                    "${stringResource(R.string.humidity)} ${weather.weatherCurrent.current.humidity}%",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium,
                    color = textColor
                )
                Text(
                    "${stringResource(R.string.rain_chance)} ${weather.weatherCurrent.current.chance_of_rain}%",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium,
                    color = textColor
                )
                Text(
                    "${stringResource(R.string.uv_index)} ${weather.weatherCurrent.current.uv}",
                    modifier = Modifier.padding(padding),
                    style = Typography.labelMedium,
                    color = textColor
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
    forecastShown: List<HourUi>,
    textColor: Color
) {
    when (forecastState) {
        is ForecastUiState.ForecastLoading -> CircularProgressIndicator()
        is ForecastUiState.ForecastSuccess -> {
            val currentHour =
                forecastState.weatherCurrent.location.localtime
                    .substringAfter(" ").substringBefore(":").toIntOrNull() ?: 0
            Card(
                modifier =
                    Modifier
                        .padding(top = 40.dp, bottom = 40.dp)
                        .fillMaxWidth(),
                colors =
                frostCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier =
                        Modifier
                            .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    forecastShown.forEach { hour ->
                        Column(
                            modifier = Modifier.padding(padding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GlideImage(
                                hour.condition.icon,
                                stringResource(R.string.weather_Icon)
                            )
                            Text(
                                hour.displayTime(currentHour),
                                color = textColor
                            )
                            Text(
                                hour.tempC,
                                color = textColor
                            )
                            Text(hour.displayDay(), color = textColor)
                        }
                    }
                }
            }
        }

        is ForecastUiState.ForecastError -> Text(forecastState.message)
    }
}

@Composable
private fun AstrologyContent(
    astrologyState: AstroUiState,
    padding: Dp,
    textColor: Color
) {
    when (astrologyState) {
        is AstroUiState.AstroLoading -> CircularProgressIndicator()
        is AstroUiState.AstroSuccess -> {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 40.dp),
                colors =
                frostCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${stringResource(R.string.sunset)} ${astrologyState.astro.astronomy.astro.sunset}",
                        color = textColor
                    )
                    Text(
                        "${stringResource(R.string.sunrise)} ${astrologyState.astro.astronomy.astro.sunrise}",
                        color = textColor
                    )
                    Text(
                        "${stringResource(R.string.moon_form)} ${astrologyState.astro.astronomy.astro.moon_phase}",
                        color = textColor
                    )
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
        backgroundRes = R.drawable.default_screen,
        textColor = Color.White,
    )
}
