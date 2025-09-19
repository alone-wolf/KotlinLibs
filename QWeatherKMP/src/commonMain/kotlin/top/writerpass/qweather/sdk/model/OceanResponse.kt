package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class OceanTideResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val tideTable: List<TideTable>,
    val tideHourly: List<TideHourly>,
    val refer: Refer,
) {
    @Serializable
    data class TideTable(
        val fxTime: String,
        val height: String,
        val type: String,
    )

    @Serializable
    data class TideHourly(
        val fxTime: String,
        val height: String?,
    )
}