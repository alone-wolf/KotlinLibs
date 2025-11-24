package top.writerpass.micromessage.server.core.data.service.chat.data

import kotlinx.serialization.Serializable

@Serializable
data class PrivateChatOption(
    val id:Long,
    val chatId:String,
    val ownerId:Long,
    val mute: Boolean,
    val pin: Boolean,
)