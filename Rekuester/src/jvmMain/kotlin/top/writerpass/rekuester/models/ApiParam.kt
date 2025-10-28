package top.writerpass.rekuester.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiParam(
    val key: String,
    val value: String,
    val description: String = "",
    val enabled: Boolean
)