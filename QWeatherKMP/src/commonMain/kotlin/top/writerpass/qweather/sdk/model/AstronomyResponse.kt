package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class AstronomySunResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val sunrise: String?,
    val sunset: String?,
    val refer: Refer,
)

@Serializable
data class AstronomyMoonResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val moonrise: String?,
    val moonset: String?,
    val moonPhase: List<MoonPhase>,
    val refer: Refer,
) {

    @Serializable
    data class MoonPhase(
        val fxTime: String,
        val value: String,
        val name: String,
        val illumination: String,
        val icon: String,
    )

}


@Serializable
data class AstronomySunElevationAngleResponse(
    val code: String,
    val solarElevationAngle: String,
    val solarAzimuthAngle: String,
    val solarHour: String,
    val hourAngle: String,
    val refer: Refer,
)