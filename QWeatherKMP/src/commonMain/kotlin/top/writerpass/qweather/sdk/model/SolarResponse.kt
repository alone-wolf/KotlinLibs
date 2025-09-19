package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable


@Serializable
data class SolarRadiationResponse(
    val metadata: Metadata,
    val forecasts: List<Forecast>,
) {


    @Serializable
    data class Metadata(
        val tag: String,
    )

    @Serializable
    data class Forecast(
        val forecastTime: String,
        val solarAngle: SolarAngle,
        val dni: Dni,
        val dhi: Dhi,
        val ghi: Ghi,
        val weather: Weather,
    )

    @Serializable
    data class SolarAngle(
        val azimuth: Long,
        val elevation: Long,
    )

    @Serializable
    data class Dni(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class Dhi(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class Ghi(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class Weather(
        val temperature: Temperature,
        val windSpeed: WindSpeed,
        val humidity: Long,
    )

    @Serializable
    data class Temperature(
        val value: Double,
        val unit: String,
    )

    @Serializable
    data class WindSpeed(
        val value: Double,
        val unit: String,
    )

}
