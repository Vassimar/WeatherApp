package app.weatherapp.system.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
internal fun SavedCitiesScreen() {
    val testURL: String = "https:" + "//cdn.weatherapi.com/weather/64x64/day/122.png"
    val cities: List<SavedCities> =
        listOf(
            SavedCities(
                1,
                "Prague",
                10.6,
                testURL
            ),
            SavedCities(
                2,
                "Prague",
                13.6,
                testURL
            ),
            SavedCities(
                3,
                "Prague",
                11.6,
                testURL
            )
        )
    SavedCitiesContent(cities)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SavedCitiesContent(cities: List<SavedCities>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(cities, key = { it.id }) { city ->

            ElevatedCard(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable(
                            enabled = true,
                            onClick = {}
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = city.name,
                    )

                    Text(
                        text = "${city.temp}",
                    )
                    GlideImage(
                        model = city.img,
                        contentDescription = "weather_icon",
                        Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}

data class SavedCities(
    val id: Int,
    val name: String,
    val temp: Double,
    val img: String
)

@Preview
@Composable
fun ContentPreview() {
    SavedCitiesContent(
        listOf(
            SavedCities(
                1,
                "Prague",
                10.6,
                "https://icons.veryicon.com/png/o/leisure/tourism-icon/a-sunny-day-1.png"
            ),
            SavedCities(
                2,
                "Prague",
                13.6,
                "https://icons-for-free.com/iff/png/512/sunny+temperature+weather+icon-1320196637430890623.png"
            ),
            SavedCities(
                3,
                "Prague",
                11.6,
                "https://icons-for-free.com/iff/png/512/sunny+temperature+weather+icon-1320196637430890623.png"
            )
        )
    )
}
