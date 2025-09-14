package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherNowResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val now: Now,
    val refer: Refer,
) {
    @Serializable
    data class Now(
        val obsTime: String,
        val temp: String,
        val feelsLike: String,
        val icon: String,
        val text: String,
        val wind360: String,
        val windDir: String,
        val windScale: String,
        val windSpeed: String,
        val humidity: String,
        val precip: String,
        val pressure: String,
        val vis: String,
        val cloud: String?,
        val dew: String?
    )
}

@Serializable
data class WeatherDaysResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val daily: List<Daily>,
    val refer: Refer,
) {
    @Serializable
    data class Daily(
        val fxDate: String,
        val sunrise: String?,
        val sunset: String?,
        val moonrise: String?,
        val moonset: String?,
        val moonPhase: String,
        val moonPhaseIcon: String,
        val tempMax: String,
        val tempMin: String,
        val iconDay: String,
        val textDay: String,
        val iconNight: String,
        val textNight: String,
        val wind360Day: String,
        val windDirDay: String,
        val windScaleDay: String,
        val windSpeedDay: String,
        val wind360Night: String,
        val windDirNight: String,
        val windScaleNight: String,
        val windSpeedNight: String,
        val humidity: String,
        val precip: String,
        val pressure: String,
        val vis: String,
        val cloud: String?,
        val uvIndex: String
    )
}

@Serializable
data class WeatherHoursResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val hourly: List<Hourly>,
    val refer: Refer,
) {
    @Serializable
    data class Hourly(
        val fxTime: String,
        val temp: String,
        val icon: String,
        val text: String,
        val wind360: String,
        val windDir: String,
        val windScale: String,
        val windSpeed: String,
        val humidity: String,
        val pop: String?,
        val precip: String,
        val pressure: String,
        val cloud: String?,
        val dew: String?
    )
}

@Serializable
data class WeatherNowGridResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val now: Now,
    val refer: Refer,
) {
    @Serializable
    data class Now(
        val obsTime: String,
        val temp: String,
        val icon: String,
        val text: String,
        val wind360: String,
        val windDir: String,
        val windScale: String,
        val windSpeed: String,
        val humidity: String,
        val precip: String,
        val pressure: String,
        val cloud: String?,
        val dew: String?
    )
}

@Serializable
data class WeatherDaysGridResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val daily: List<Daily>,
    val refer: Refer,
) {
    @Serializable
    data class Daily(
        val fxDate: String,
        val tempMax: String,
        val tempMin: String,
        val iconDay: String,
        val iconNight: String,
        val textDay: String,
        val textNight: String,
        val wind360Day: String,
        val windDirDay: String,
        val windScaleDay: String,
        val windSpeedDay: String,
        val wind360Night: String,
        val windDirNight: String,
        val windScaleNight: String,
        val windSpeedNight: String,
        val humidity: String,
        val precip: String,
        val pressure: String
    )
}

@Serializable
data class WeatherHoursGridResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val hourly: List<Hourly>,
    val refer: Refer,
) {
    @Serializable
    data class Hourly(
        val fxTime: String,
        val temp: String,
        val icon: String,
        val text: String,
        val wind360: String,
        val windDir: String,
        val windScale: String,
        val windSpeed: String,
        val humidity: String,
        val precip: String,
        val pressure: String,
        val cloud: String?,
        val dew: String?
    )
}




