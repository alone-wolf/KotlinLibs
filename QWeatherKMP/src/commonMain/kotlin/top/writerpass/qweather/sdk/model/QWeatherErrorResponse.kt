package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class QWeatherErrorResponse(
    val status: String,
    val type: String,
    val title: String,
    val detail: String,
    val invalidParams: List<String>? = null
)