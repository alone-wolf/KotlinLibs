package top.writerpass.micromessage.server.core.data.service.auth.request

data class LoginRequest(
    val account: String,
    val password: String
)