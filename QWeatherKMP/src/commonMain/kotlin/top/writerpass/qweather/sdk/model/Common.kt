package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class Refer(
    val sources: List<String>?,
    val license: List<String>?,
)