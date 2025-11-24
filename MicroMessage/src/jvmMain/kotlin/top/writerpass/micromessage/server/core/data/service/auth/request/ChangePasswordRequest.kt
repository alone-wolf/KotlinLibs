package top.writerpass.micromessage.server.core.data.service.auth.request

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)