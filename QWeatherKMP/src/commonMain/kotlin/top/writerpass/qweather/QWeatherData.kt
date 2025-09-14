package top.writerpass.qweather

import kotlinx.serialization.Serializable

object QWeatherData {
    @Serializable
    data class Refer(
        val sources: List<String>?,
        val license: List<String>?
    )


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