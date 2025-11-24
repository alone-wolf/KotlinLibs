package top.writerpass.micromessage.server.core.data.service.user.request

data class UpdateProfileRequest(
    val nickname: String?,
    val avatarUrl: String?,
    val description: String?
)