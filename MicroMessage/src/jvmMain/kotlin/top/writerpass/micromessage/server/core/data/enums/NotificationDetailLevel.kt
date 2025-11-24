package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class NotificationDetailLevel {
    None, // 只显示有通知
    Sender, // 只显示发送者名称
    All // 显示发送者+消息内容
}