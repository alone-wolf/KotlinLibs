package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable


@Serializable
data class AirQualityCurrentResponse(
    val metadata: Metadata,
    val indexes: List<Index>,
    val pollutants: List<Pollutant>,
    val stations: List<Station>,
) {
    @Serializable
    data class Metadata(
        val tag: String,
    )

    @Serializable
    data class Index(
        val code: String,
        val name: String,
        val aqi: Double,
        val aqiDisplay: String,
        val level: String,
        val category: String,
        val color: Color,
        val primaryPollutant: PrimaryPollutant,
        val health: Health,
    )

    @Serializable
    data class Color(
        val red: Long,
        val green: Long,
        val blue: Long,
        val alpha: Long,
    )

    @Serializable
    data class PrimaryPollutant(
        val code: String?,
        val name: String?,
        val fullName: String?,
    )

    @Serializable
    data class Health(
        val effect: String?,
        val advice: Advice,
    )

    @Serializable
    data class Advice(
        val generalPopulation: String?,
        val sensitivePopulation: String?,
    )

    @Serializable
    data class Pollutant(
        val code: String,
        val name: String,
        val fullName: String,
        val concentration: Concentration,
        val subIndexes: List<SubIndex>,
    )

    @Serializable
    data class Concentration(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class SubIndex(
        val code: String?,
        val aqi: Double?,
        val aqiDisplay: String,
    )

    @Serializable
    data class Station(
        val id: String,
        val name: String,
    )

}

@Serializable
data class AirQualityHourlyResponse(
    val metadata: Metadata,
    val hours: List<Hour>,
) {

    @Serializable
    data class Metadata(
        val tag: String,
    )

    @Serializable
    data class Hour(
        val forecastTime: String,
        val indexes: List<Index>,
        val pollutants: List<Pollutant>,
    )

    @Serializable
    data class Index(
        val code: String,
        val name: String,
        val aqi: Double,
        val aqiDisplay: String,
        val level: String?,
        val category: String?,
        val color: Color,
        val primaryPollutant: PrimaryPollutant,
        val health: Health,
    )

    @Serializable
    data class Color(
        val red: Long,
        val green: Long,
        val blue: Long,
        val alpha: Long,
    )

    @Serializable
    data class PrimaryPollutant(
        val code: String?,
        val name: String?,
        val fullName: String?,
    )

    @Serializable
    data class Health(
        val effect: String?,
        val advice: Advice,
    )

    @Serializable
    data class Advice(
        val generalPopulation: String?,
        val sensitivePopulation: String?,
    )

    @Serializable
    data class Pollutant(
        val code: String,
        val name: String,
        val fullName: String,
        val concentration: Concentration,
        val subIndexes: List<SubIndex>,
    )

    @Serializable
    data class Concentration(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class SubIndex(
        val code: String?,
        val aqi: Double?,
        val aqiDisplay: String,
    )

}


@Serializable
data class AirQualityDailyResponse(
    val metadata: Metadata,
    val days: List<Day>,
) {

    @Serializable
    data class Metadata(
        val tag: String,
    )

    @Serializable
    data class Day(
        val forecastStartTime: String,
        val forecastEndTime: String,
        val indexes: List<Index>,
        val pollutants: List<Pollutant>,
    )

    @Serializable
    data class Index(
        val code: String,
        val name: String,
        val aqi: Double,
        val aqiDisplay: String,
        val level: String?,
        val category: String?,
        val color: Color,
        val primaryPollutant: PrimaryPollutant,
        val health: Health,
    )

    @Serializable
    data class Color(
        val red: Long,
        val green: Long,
        val blue: Long,
        val alpha: Long,
    )

    @Serializable
    data class PrimaryPollutant(
        val code: String?,
        val name: String?,
        val fullName: String?,
    )

    @Serializable
    data class Health(
        val effect: String?,
        val advice: Advice,
    )

    @Serializable
    data class Advice(
        val generalPopulation: String?,
        val sensitivePopulation: String?,
    )

    @Serializable
    data class Pollutant(
        val code: String,
        val name: String,
        val fullName: String,
        val concentration: Concentration,
        val subIndexes: List<SubIndex>,
    )

    @Serializable
    data class Concentration(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class SubIndex(
        val code: String,
        val aqi: Double,
        val aqiDisplay: String,
    )

}

//@Serializable
//data class AirQualityStationResponse(
//
//) {
//
//}


