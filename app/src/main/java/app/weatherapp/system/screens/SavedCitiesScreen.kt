package app.weatherapp.system.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.weatherapp.R
import app.weatherapp.domain.model.SavedCities
import app.weatherapp.presentation.SavedCitiesScreenViewModel
import app.weatherapp.ui.theme.getTextColor
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SavedCitiesScreen(
    viewModel: SavedCitiesScreenViewModel = koinViewModel(),
    onNavigate: (String) -> Unit,
) {
    val cities by viewModel.savedCities.collectAsState()
    Box(
        modifier =
            Modifier
                .fillMaxSize()
    )
    SavedCitiesContent(
        cities,
        { cityName -> viewModel.removeCity(cityName) },
        onNavigate,
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SavedCitiesContent(
    cities: List<SavedCities>,
    onSwipeDelete: (String) -> Unit,
    onNavigate: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(cities, key = { it.cityKey }) { city ->
            val textColor = getTextColor(city.isDay)
            val dismissState =
                rememberSwipeToDismissBoxState(
                    positionalThreshold = { it * 0.8f }
                )

            LaunchedEffect(dismissState.currentValue) {
                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                    onSwipeDelete(city.cityKey)
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {}
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(4.dp)
                ) {
                    Image(
                        painterResource(city.code),
                        contentDescription = stringResource(R.string.default_background),
                        modifier =
                            Modifier
                                .matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(8.dp)
                            .clickable(
                                enabled = true,
                                onClick = { onNavigate(city.cityKey) }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = city.name,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 32.dp),
                                color = textColor
                            )
                            Text(
                                city.time,
                                textAlign = TextAlign.Center,
                                color = textColor
                            )
                        }

                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = city.temp,
                                color = textColor
                            )
                        }
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GlideImage(
                                model = city.img,
                                contentDescription = stringResource(R.string.weather_Icon),
                                Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    SavedCitiesContent(
        listOf(
            SavedCities(
                cityKey = "",
                code = R.drawable.default_screen,
                "Barcelona",
                "10.6",
                "https://icons.veryicon.com/png/o/leisure/tourism-icon/a-sunny-day-1.png",
                time = "22:00",
                isDay = 1
            ),
            SavedCities(
                cityKey = "",
                code = R.drawable.snowy_screen,
                "Prague",
                "13.6",
                "https://icons-for-free.com/iff/png/512/sunny+temperature+weather+icon-1320196637430890623.png",
                time = "22:00",
                isDay = 0
            ),
            SavedCities(
                cityKey = "",
                code = R.drawable.thunder_screen,
                "BANGOGOGOKo",
                "11.6",
                "https://icons-for-free.com/iff/png/512/sunny+temperature+weather+icon-1320196637430890623.png",
                time = "22:00",
                isDay = 1
            )
        ),
        onSwipeDelete = {},
        onNavigate = {},
    )
}
