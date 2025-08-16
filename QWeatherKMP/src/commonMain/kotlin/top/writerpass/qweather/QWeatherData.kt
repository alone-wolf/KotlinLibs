package top.writerpass.qweather

import kotlinx.serialization.Serializable

object QWeatherData {
    @Serializable
    data class Refer(
        val sources: List<String>?,
        val license: List<String>?
    )

    object Now {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val now: NowData,
            val refer: Refer
        )

        @Serializable
        data class NowData(
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

    object Daily {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val daily: List<DailyData>,
            val refer: Refer
        )

        @Serializable
        data class DailyData(
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

    object Hourly {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val hourly: List<HourlyData>,
            val refer: Refer
        )

        @Serializable
        data class HourlyData(
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

    object GridNow {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val now: NowData,
            val refer: Refer
        )

        @Serializable
        data class NowData(
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

    object GridDaily {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val daily: List<Daily>,
            val refer: Refer
        )

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

    object GridHourly {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val hourly: List<HourlyData>,
            val refer: Refer
        )

        @Serializable
        data class HourlyData(
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

    object PrecipitationMinutely {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val summary: String,
            val minutely: List<MinutelyData>,
            val refer: Refer
        )

        @Serializable
        data class MinutelyData(
            val fxTime: String,
            val precip: String,
            val type: String
        )
    }

    object Warning {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val fxLink: String,
            val warning: List<WarningData>?,
            val refer: Refer
        )

        @Serializable
        data class WarningData(
            val id: String,
            val sender: String?,
            val pubTime: String,
            val title: String,
            val startTime: String?,
            val endTime: String?,
            val status: String,
//            val level: String?,
            val severity: String,
            val severityColor: String?,
            val type: String,
            val typeName: String,
            val urgency: String?,
            val certainty: String?,
            val text: String,
            val related: String?
        )
    }

    object WarningList {
        @Serializable
        data class ReturnBody(
            val code: String,
            val updateTime: String,
            val warningLOCList: List<WarningLOCList>,
            val refer: Refer
        )

        @Serializable
        data class WarningLOCList(
            val locationID: String
        )
    }

    object AirQualityCurrently {
        @Serializable
        data class ReturnBody(
            val metadata: Metadata,
            val indexes: List<Index>,
            val pollutants: List<Pollutant>,
            val stations: List<Station>
        )

        @Serializable
        data class Index(
            val code: String,
            val name: String,
            val aqi: Float,
            val aqiDisplay: String,
            val level: String?,
            val category: String?,
            val color: Color,
            val primaryPollutant: PrimaryPollutant,
            val health: Health
        )

        @Serializable
        data class Color(
            val red: Long,
            val green: Long,
            val blue: Long,
            val alpha: Long
        )

        @Serializable
        data class Health(
            val effect: String?,
            val advice: Advice
        )

        @Serializable
        data class Advice(
            val generalPopulation: String?,
            val sensitivePopulation: String?
        )

        @Serializable
        data class PrimaryPollutant(
            val code: String?,
            val name: String?,
            val fullName: String?
        )

        @Serializable
        data class Metadata(
            val tag: String
        )

        @Serializable
        data class Pollutant(
            val code: String,
            val name: String,
            val fullName: String,
            val concentration: Concentration,
            val subIndexes: List<SubIndex>
        )

        @Serializable
        data class Concentration(
            val value: Double,
            val unit: String
        )

        @Serializable
        data class SubIndex(
            val code: String?,
            val aqi: Float?,
            val aqiDisplay: String
        )

        @Serializable
        data class Station(
            val id: String?,
            val name: String
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