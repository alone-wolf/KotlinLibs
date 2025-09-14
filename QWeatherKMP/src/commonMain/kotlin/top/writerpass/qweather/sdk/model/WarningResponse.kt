package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class WarningNowResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val warning: List<Warning>,
    val refer: Refer,
) {
    @Serializable
    data class Warning(
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

@Serializable
data class WarningListResponse(
    val code: String,
    val updateTime: String,
    val warningLocList: List<WarningLoc>,
    val refer: Refer,
) {
    @Serializable
    data class WarningLoc(
        val locationId: String,
    )
}