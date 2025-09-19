package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class IndicesResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val daily: List<Daily>,
    val refer: Refer,
) {
    @Serializable
    data class Daily(
        val date: String,
        val type: String,
        val name: String,
        val level: String,
        val category: String,
        val text: String?,
    )
}