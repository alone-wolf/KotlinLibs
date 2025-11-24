package top.writerpass.micromessage.server.core.data.service.friend.data

import kotlinx.serialization.Serializable

@Serializable
enum class
FriendStatus {
    Pending, Accepted, Rejected
}