package top.writerpass.micromessage.server.core.data.service.auth.principal

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.server.core.data.service.auth.data.LoginSession

@Serializable
class UserInfoPrincipal(
    val userId: Long,
    val session: LoginSession,
)