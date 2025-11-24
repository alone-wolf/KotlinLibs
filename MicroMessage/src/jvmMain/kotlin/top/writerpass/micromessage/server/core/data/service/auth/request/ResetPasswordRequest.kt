package top.writerpass.micromessage.server.core.data.service.auth.request

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)