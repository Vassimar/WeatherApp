package app.weatherapp.network.mapper

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
import app.weatherapp.network.networkData.Astro
import app.weatherapp.network.networkData.Astronomy
import app.weatherapp.network.networkData.AstronomyWeatherModel
import app.weatherapp.network.networkData.Condition
import app.weatherapp.network.networkData.Current
import app.weatherapp.network.networkData.CurrentWeatherModel
import app.weatherapp.network.networkData.Day
import app.weatherapp.network.networkData.Forecast
import app.weatherapp.network.networkData.ForecastDay
import app.weatherapp.network.networkData.ForecastWeatherModel
import app.weatherapp.network.networkData.Hour
import app.weatherapp.network.networkData.Location

internal fun Astro.toDomain(): AstroUi {
    return AstroUi(
        is_moon_up = is_moon_up,
        is_sun_up = is_sun_up,
        moon_illumination = moon_illumination,
        moon_phase = moon_phase,
        moonrise = moonrise,
        moonset = moonset,
        sunrise = sunrise,
        sunset = sunset
    )
}

internal fun AstroUi.toApi(): Astro {
    return Astro(
        is_moon_up = is_moon_up,
        is_sun_up = is_sun_up,
        moon_illumination = moon_illumination,
        moon_phase = moon_phase,
        moonrise = moonrise,
        moonset = moonset,
        sunrise = sunrise,
        sunset = sunset
    )
}

internal fun Astronomy.toDomain(): AstronomyUi =
    AstronomyUi(
        astro = astro.toDomain()
    )

internal fun AstronomyUi.toApi(): Astronomy =
    Astronomy(
        astro = astro.toApi()
    )

internal fun Condition.toDomain(): ConditionUI =
    ConditionUI(
        code = code,
        icon = "https:$icon",
        text = text
    )

internal fun ConditionUI.toApi(): Condition =
    Condition(
        code = code,
        icon = icon.removePrefix("https:"),
        text = text
    )

internal fun Location.toDomain(): LocationUi =
    LocationUi(
        country = country,
        lat = lat,
        localtime = localtime,
        localtime_epoch = localtime_epoch,
        lon = lon,
        name = name,
        region = region,
        tz_id = tz_id
    )

internal fun LocationUi.toApi(): Location =
    Location(
        country = country,
        lat = lat,
        localtime = localtime,
        localtime_epoch = localtime_epoch,
        lon = lon,
        name = name,
        region = region,
        tz_id = tz_id
    )

internal fun Current.toDomain(): CurrentUi =
    CurrentUi(
        chance_of_rain = chance_of_rain,
        chance_of_snow = chance_of_snow,
        cloud = cloud,
        condition = condition.toDomain(),
        feelslike_c = feelslike_c,
        feelslike_f = feelslike_f,
        humidity = humidity,
        is_day = is_day,
        last_updated = last_updated,
        last_updated_epoch = last_updated_epoch,
        temp_c = "$temp_c°C",
        temp_f = "$temp_f°F",
        will_it_snow = will_it_snow,
        uv = uv
    )

internal fun CurrentUi.toApi(): Current =
    Current(
        chance_of_rain = chance_of_rain,
        chance_of_snow = chance_of_snow,
        cloud = cloud,
        condition = condition.toApi(),
        feelslike_c = feelslike_c,
        feelslike_f = feelslike_f,
        humidity = humidity,
        is_day = is_day,
        last_updated = last_updated,
        last_updated_epoch = last_updated_epoch,
        temp_c = temp_c.removeSuffix("°C").toDouble(),
        temp_f = temp_f.removeSuffix("°F").toDouble(),
        will_it_snow = will_it_snow,
        uv = uv
    )

internal fun CurrentWeatherModel.toDomain(): CurrentWeather {
    return CurrentWeather(
        current = current.toDomain(),
        location = location.toDomain()
    )
}

internal fun CurrentWeather.toApi(): CurrentWeatherModel {
    return CurrentWeatherModel(
        current = current.toApi(),
        location = location.toApi()
    )
}

internal fun AstronomyWeatherModel.toDomain(): AstronomyWeather {
    return AstronomyWeather(
        astronomy = astronomy.toDomain(),
        location = location.toDomain()
    )
}

internal fun AstronomyWeather.toApi(): AstronomyWeatherModel {
    return AstronomyWeatherModel(
        astronomy = astronomy.toApi(),
        location = location.toApi()
    )
}

internal fun Day.toDomain(): DayUi =
    DayUi(
        avgHumidity = avghumidity,
        avgTempC = avgtemp_c,
        avgTempF = avgtemp_f,
        avgVisKm = avgvis_km,
        avgVisMiles = avgvis_miles,
        condition = condition.toDomain(),
        dailyChanceOfRain = daily_chance_of_rain,
        dailyChanceOfSnow = daily_chance_of_snow,
        dailyWillItRain = daily_will_it_rain,
        dailyWillItSnow = daily_will_it_snow,
        maxTempC = maxtemp_c,
        maxTempF = maxtemp_f,
        maxWindKph = maxwind_kph,
        maxWindMph = maxwind_mph,
        minTempC = mintemp_c,
        minTempF = mintemp_f,
        totalPrecipIn = totalprecip_in,
        totalPrecipMm = totalprecip_mm,
        totalSnowCm = totalsnow_cm,
        uv = uv
    )

internal fun DayUi.toApi(): Day =
    Day(
        avghumidity = avgHumidity,
        avgtemp_c = avgTempC,
        avgtemp_f = avgTempF,
        avgvis_km = avgVisKm,
        avgvis_miles = avgVisMiles,
        condition = condition.toApi(),
        daily_chance_of_rain = dailyChanceOfRain,
        daily_chance_of_snow = dailyChanceOfSnow,
        daily_will_it_rain = dailyWillItRain,
        daily_will_it_snow = dailyWillItSnow,
        maxtemp_c = maxTempC,
        maxtemp_f = maxTempF,
        maxwind_kph = maxWindKph,
        maxwind_mph = maxWindMph,
        mintemp_c = minTempC,
        mintemp_f = minTempF,
        totalprecip_in = totalPrecipIn,
        totalprecip_mm = totalPrecipMm,
        totalsnow_cm = totalSnowCm,
        uv = uv
    )

internal fun Hour.toDomain(): HourUi =
    HourUi(
        chanceOfRain = chance_of_rain,
        chanceOfSnow = chance_of_snow,
        cloud = cloud,
        condition = condition.toDomain(),
        dewPointC = dewpoint_c,
        dewPointF = dewpoint_f,
        feelsLikeC = feelslike_c,
        feelsLikeF = feelslike_f,
        gustKph = gust_kph,
        gustMph = gust_mph,
        heatIndexC = heatindex_c,
        heatIndexF = heatindex_f,
        humidity = humidity,
        isDay = is_day,
        precipIn = precip_in,
        precipMm = precip_mm,
        pressureIn = pressure_in,
        pressureMb = pressure_mb,
        snowCm = snow_cm,
        tempC = "$temp_c°C",
        tempF = "$temp_f°F",
        time = time,
        timeEpoch = time_epoch,
        uv = uv,
        visKm = vis_km,
        visMiles = vis_miles,
        willItRain = will_it_rain,
        willItSnow = will_it_snow,
        windDegree = wind_degree,
        windDir = wind_dir,
        windKph = wind_kph,
        windMph = wind_mph,
        windChillC = windchill_c,
        windChillF = windchill_f
    )

internal fun HourUi.toApi(): Hour =
    Hour(
        chance_of_rain = chanceOfRain,
        chance_of_snow = chanceOfSnow,
        cloud = cloud,
        condition = condition.toApi(),
        dewpoint_c = dewPointC,
        dewpoint_f = dewPointF,
        feelslike_c = feelsLikeC,
        feelslike_f = feelsLikeF,
        gust_kph = gustKph,
        gust_mph = gustMph,
        heatindex_c = heatIndexC,
        heatindex_f = heatIndexF,
        humidity = humidity,
        is_day = isDay,
        precip_in = precipIn,
        precip_mm = precipMm,
        pressure_in = pressureIn,
        pressure_mb = pressureMb,
        snow_cm = snowCm,
        temp_c = tempC.removeSuffix("°C").toDouble(),
        temp_f = tempF.removeSuffix("°F").toDouble(),
        time = time,
        time_epoch = timeEpoch,
        uv = uv,
        vis_km = visKm,
        vis_miles = visMiles,
        will_it_rain = willItRain,
        will_it_snow = willItSnow,
        wind_degree = windDegree,
        wind_dir = windDir,
        wind_kph = windKph,
        wind_mph = windMph,
        windchill_c = windChillC,
        windchill_f = windChillF
    )

internal fun ForecastDay.toDomain(): ForecastDayUi =
    ForecastDayUi(
        astro = astro?.toDomain(),
        date = date,
        dateEpoch = date_epoch,
        day = day.toDomain(),
        hour = hour.map { it.toDomain() }
    )

internal fun ForecastDayUi.toApi(): ForecastDay =
    ForecastDay(
        astro = astro?.toApi(),
        date = date,
        date_epoch = dateEpoch,
        day = day.toApi(),
        hour = hour.map { it.toApi() }
    )

internal fun Forecast.toDomain(): ForecastUi =
    ForecastUi(
        forecastDays = forecastDay.map { it.toDomain() }
    )

internal fun ForecastUi.toApi(): Forecast =
    Forecast(
        forecastDay = forecastDays.map { it.toApi() }
    )

internal fun ForecastWeatherModel.toDomain(): ForecastWeather =
    ForecastWeather(
        current = current.toDomain(),
        forecast = forecast.toDomain(),
        location = location.toDomain()
    )

internal fun ForecastWeather.toApi(): ForecastWeatherModel =
    ForecastWeatherModel(
        current = current.toApi(),
        forecast = forecast.toApi(),
        location = location.toApi()
    )
