package top.writerpass.micromessage.server.core.data.service.notification.data

import kotlinx.serialization.Serializable


@Serializable
data class Notification(
    val id: Long,
    val receiverId: Long,
    val title:String,
    val content:String,
)
