package top.writerpass.micromessage.server.core.data.service.chat.data

import kotlinx.serialization.Serializable

@Serializable
enum class ChatType {
    Private,
    Group,
    Temp
}


@Serializable
data class Chat(
    val id: Long,
    val type: ChatType,
    val name: String?,
    val description: String,
    val avatarUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
)

@Serializable
data class ChatMember(
    val id: Long,
    val chatId: Long,
    val userId: Long,
    val role: String,
    val mute: Boolean,
    val pin: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)

@Serializable
enum class ChatMemberRole {
    Owner,
    Admin,
    Member
}

@Serializable
data class ChatMessage(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val content: String,
    val messageType: ChatMessageType,
    val timestamp: Long,
    val createdAt: Long,
)

@Serializable
enum class ChatMessageType {
    Text,
    Image,
    Video,
    File,
    Voice,
    Sticker,
    Location,
    Contact,
    System,
    Custom
}