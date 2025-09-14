package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class PrecipitationMinutesResponse(
    val updateTime: String,
    val fxLink: String,
    val summary: String,
    val minutely: List<Minutely>,
    val refer: Refer,
) {
    @Serializable
    data class Minutely(
        val fxTime: String,
        val precip: String,
        val type: String
    )
}