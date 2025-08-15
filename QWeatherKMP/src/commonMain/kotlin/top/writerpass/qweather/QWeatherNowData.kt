package top.writerpass.qweather

import kotlinx.serialization.Serializable

@Serializable
data class QWeatherNowData(
    val obsTime: String,
    val temp: String,
    val feelsLike: String,
    val icon: String? = null,
    val text: String,
    val wind360: String? = null,
    val windDir: String? = null,
    val windScale: String? = null,
    val windSpeed: String? = null,
    val humidity: String? = null,
    val precip: String? = null,
    val pressure: String? = null,
    val vis: String? = null,
    val cloud: String? = null
)