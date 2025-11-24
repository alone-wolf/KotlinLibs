package top.writerpass.micromessage.server.core.data.service.chat.data

import kotlinx.serialization.Serializable


@Serializable
data class GroupChat(
    val id:Long,
    val name:String,
    val ownerId:Long,
    val createdAt:Long,
)

