package top.writerpass.qweather

import kotlinx.serialization.Serializable


object QWeatherData {
    @Serializable
    data class Refer (
        val sources: List<String>?,
        val license: List<String>?
    )
    object Now{
        @Serializable
        data class ReturnBody (
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val now: NowData,
            val refer: Refer
        )
        @Serializable
        data class NowData (
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

    object Daily{
        @Serializable
        data class ReturnBody (
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val daily: List<DailyData>,
            val refer: Refer
        )

        @Serializable
        data class DailyData (
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

    object Hourly{
        @Serializable
        data class ReturnBody (
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val hourly: List<HourlyData>,
            val refer: Refer
        )

        @Serializable
        data class HourlyData (
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

    object Minutely{
        @Serializable
        data class ReturnBody (
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val summary: String,
            val minutely: List<MinutelyData>,
            val refer: Refer
        )

        @Serializable
        data class MinutelyData (
            val fxTime: String,
            val precip: String,
            val type: String
        )
    }

    @Serializable
    data class QWeatherReturnErrorBody(
        val error: QWeatherReturnError
    ) {
        @Serializable
        data class QWeatherReturnError(
            val status: String,
            val type: String,
            val title: String,
            val detail: String,
            val invalidParams: List<String>? = null
        )
    }
}