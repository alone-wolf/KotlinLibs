package top.writerpass.micromessage.common.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val passwordHash0: String,
)
