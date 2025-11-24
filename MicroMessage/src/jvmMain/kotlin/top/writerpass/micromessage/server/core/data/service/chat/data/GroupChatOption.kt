package top.writerpass.micromessage.server.core.data.service.chat.data

import kotlinx.serialization.Serializable

@Serializable
data class GroupChatOption(
    val id: Long,
    val chatId: Long, // GroupChat.id
    val ownerId: Long, // User.id
    val mute: Boolean,
    val pin: Boolean,
)