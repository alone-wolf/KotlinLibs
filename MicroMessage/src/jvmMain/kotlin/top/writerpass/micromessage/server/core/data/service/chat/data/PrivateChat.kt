package top.writerpass.micromessage.server.core.data.service.chat.data

import kotlinx.serialization.Serializable

@Serializable
data class PrivateChat(
    val id: String, // "${user.id.min}-${user.id.max}
    val createdAt: Long,
)



