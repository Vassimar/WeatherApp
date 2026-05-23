package app.weatherapp.system.components

import app.weatherapp.domain.model.HourUi

fun HourUi.displayTime(currentHour: Int): String {
    val hourOfDay = time.substringAfter(" ").substringBefore(":").toIntOrNull() ?: -1
    return if (hourOfDay == currentHour) "Now" else time.substringAfter(" ")
}

fun HourUi.displayDay(): String = time.substringAfter("-").substringBefore(" ")
