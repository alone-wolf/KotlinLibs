package top.writerpass.rekuester.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiHeader(
    val key: String,
    val value: String,
    val description: String = "",
    val enabled: Boolean
)