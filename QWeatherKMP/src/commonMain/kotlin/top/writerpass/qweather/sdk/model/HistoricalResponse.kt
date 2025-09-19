package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoricalWeatherResponse(
    val code: String,
    val fxLink: String,
    val weatherDaily: WeatherDaily,
    val weatherHourly: List<WeatherHourly>,
    val refer: Refer,
) {
    @Serializable
    data class WeatherDaily(
        val date: String,
        val sunrise: String?,
        val sunset: String?,
        val moonrise: String?,
        val moonset: String?,
        val moonPhase: String,
        val tempMax: String,
        val tempMin: String,
        val humidity: String,
        val precip: String,
        val pressure: String,
    )

    @Serializable
    data class WeatherHourly(
        val time: String,
        val temp: String,
        val icon: String,
        val text: String,
        val precip: String,
        val wind360: String,
        val windDir: String,
        val windScale: String,
        val windSpeed: String,
        val humidity: String,
        val pressure: String,
    )
}

@Serializable
data class HistoricalAirResponse(
    val code: String,
    val fxLink: String,
    val airHourly: List<AirHourly>,
    val refer: Refer,
) {
    @Serializable
    data class AirHourly(
        val pubTime: String,
        val aqi: String,
        val level: String,
        val category: String,
        val primary: String,
        val pm10: String,
        val pm2p5: String,
        val no2: String,
        val so2: String,
        val co: String,
        val o3: String,
    )
}


