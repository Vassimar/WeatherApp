package app.weatherapp.domain.model

data class ForecastDayUi(
    val astro: AstroUi?=null,
    val date: String,
    val dateEpoch: Int,
    val day: DayUi,
    val hour: List<HourUi>
)
