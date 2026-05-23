package app.weatherapp.system.components

import app.weatherapp.domain.model.AstroUi
import app.weatherapp.domain.model.AstronomyUi
import app.weatherapp.domain.model.AstronomyWeather
import app.weatherapp.domain.model.ConditionUI
import app.weatherapp.domain.model.CurrentUi
import app.weatherapp.domain.model.CurrentWeather
import app.weatherapp.domain.model.DayUi
import app.weatherapp.domain.model.ForecastDayUi
import app.weatherapp.domain.model.ForecastUi
import app.weatherapp.domain.model.ForecastWeather
import app.weatherapp.domain.model.HourUi
import app.weatherapp.domain.model.LocationUi

internal val fakeWeather =
    CurrentWeather(
        location = LocationUi(
            country = "Czech Republic",
            lat = 50.08,
            localtime = "2026-05-20 14:30",
            localtime_epoch = 1747747800,
            lon = 14.44,
            name = "Prague",
            region = "Hlavni mesto Praha",
            tz_id = "Europe/Prague"
        ),
        current = CurrentUi(
            chance_of_rain = 20,
            chance_of_snow = 0,
            cloud = 25,
            condition = ConditionUI(
                code = 1000,
                icon = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
                text = "Sunny"
            ),
            feelslike_c = 21.3,
            feelslike_f = 70.3,
            humidity = 55,
            is_day = 1,
            last_updated = "2026-05-20 14:15",
            last_updated_epoch = 1747746900,
            temp_c = "22.0",
            temp_f = "71.6",
            will_it_snow = 0,
            uv = 4.5
        )
    )

internal val fakeAstrology =
    AstronomyWeather(
        astronomy = AstronomyUi(
            astro = AstroUi(
                is_moon_up = 0,
                is_sun_up = 1,
                moon_illumination = 14,
                moon_phase = "Waxing Crescent",
                moonset = "12:22 AM",
                sunrise = "05:04 AM",
                sunset = "08:36 PM",
                moonrise = "08:03 AM"
            )
        ),
        location = LocationUi(
            country = "Czech Republic",
            lat = 49.2,
            localtime = "2026-05-20 18:24",
            localtime_epoch = 1779294270,
            lon = 16.6333,
            name = "Brno",
            region = "Jihomoravsky kraj",
            tz_id = "Europe/Prague"
        )
    )
internal val fakeForecastWeather = ForecastWeather(
    current = CurrentUi(
        chance_of_rain = 20,
        chance_of_snow = 0,
        cloud = 25,
        condition = ConditionUI(
            code = 1000,
            icon = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
            text = "Sunny"
        ),
        feelslike_c = 21.3,
        feelslike_f = 70.3,
        humidity = 55,
        is_day = 1,
        last_updated = "2026-05-23 14:15",
        last_updated_epoch = 1747746900,
        temp_c = "22.0°C",
        temp_f = "71.6°F",
        will_it_snow = 0,
        uv = 4.5
    ),
    location = LocationUi(
        country = "Czech Republic",
        lat = 49.2,
        localtime = "2026-05-23 14:30",
        localtime_epoch = 1779294270,
        lon = 16.6333,
        name = "Brno",
        region = "Jihomoravsky kraj",
        tz_id = "Europe/Prague"
    ),
    forecast = ForecastUi(
        forecastDays = listOf(
            ForecastDayUi(
                date = "2026-05-23",
                dateEpoch = 1779494400,
                astro = AstroUi(
                    is_moon_up = 0,
                    is_sun_up = 1,
                    moon_illumination = 14,
                    moon_phase = "Waxing Crescent",
                    moonrise = "08:03 AM",
                    moonset = "12:22 AM",
                    sunrise = "05:04 AM",
                    sunset = "08:36 PM"
                ),
                day = DayUi(
                    avgHumidity = 65,
                    avgTempC = 21.6,
                    avgTempF = 70.9,
                    avgVisKm = 9.9,
                    avgVisMiles = 6.0,
                    condition = ConditionUI(
                        code = 1063,
                        icon = "https://cdn.weatherapi.com/weather/64x64/day/176.png",
                        text = "Patchy rain nearby"
                    ),
                    dailyChanceOfRain = 87,
                    dailyChanceOfSnow = 0,
                    dailyWillItRain = 1,
                    dailyWillItSnow = 0,
                    maxTempC = 27.5,
                    maxTempF = 81.5,
                    maxWindKph = 13.7,
                    maxWindMph = 8.5,
                    minTempC = 16.0,
                    minTempF = 60.8,
                    totalPrecipIn = 0.04,
                    totalPrecipMm = 0.99,
                    totalSnowCm = 0.0,
                    uv = 6.5
                ),
                hour = listOf(
                    HourUi(
                        chanceOfRain = 0,
                        chanceOfSnow = 0,
                        cloud = 10,
                        condition = ConditionUI(
                            code = 1000,
                            icon = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
                            text = "Sunny"
                        ),
                        dewPointC = 10.0,
                        dewPointF = 50.0,
                        feelsLikeC = 18.0,
                        feelsLikeF = 64.4,
                        gustKph = 10.0,
                        gustMph = 6.2,
                        heatIndexC = 19.0,
                        heatIndexF = 66.2,
                        humidity = 55,
                        isDay = 1,
                        precipIn = 0.0,
                        precipMm = 0.0,
                        pressureIn = 29.9,
                        pressureMb = 1013.0,
                        snowCm = 0.0,
                        tempC = "19.0",
                        tempF = "66.2",
                        time = "2026-05-23 15:00",
                        timeEpoch = 1779501600,
                        uv = 4.0,
                        visKm = 10.0,
                        visMiles = 6.0,
                        willItRain = 0,
                        willItSnow = 0,
                        windDegree = 180,
                        windDir = "S",
                        windKph = 12.0,
                        windMph = 7.5,
                        windChillC = 18.0,
                        windChillF = 64.4
                    )
                )
            )
        )
    )
)
internal val fakeHourUi = listOf(
    HourUi(
        chanceOfRain = 0,
        chanceOfSnow = 0,
        cloud = 10,
        condition = ConditionUI(
            code = 1000,
            icon = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
            text = "Sunny"
        ),
        dewPointC = 10.0,
        dewPointF = 50.0,
        feelsLikeC = 18.0,
        feelsLikeF = 64.4,
        gustKph = 10.0,
        gustMph = 6.2,
        heatIndexC = 19.0,
        heatIndexF = 66.2,
        humidity = 55,
        isDay = 1,
        precipIn = 0.0,
        precipMm = 0.0,
        pressureIn = 29.9,
        pressureMb = 1013.0,
        snowCm = 0.0,
        tempC = "19.0",
        tempF = "66.2",
        time = "2026-05-23 15:00",
        timeEpoch = 1779501600,
        uv = 4.0,
        visKm = 10.0,
        visMiles = 6.0,
        willItRain = 0,
        willItSnow = 0,
        windDegree = 180,
        windDir = "S",
        windKph = 12.0,
        windMph = 7.5,
        windChillC = 18.0,
        windChillF = 64.4
    )
)
