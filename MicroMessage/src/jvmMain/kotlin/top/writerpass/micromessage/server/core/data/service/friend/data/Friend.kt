package top.writerpass.micromessage.server.core.data.service.friend.data

import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val id: Long,
    val userId: Long,
    val user1Id: Long,
    val status: FriendStatus,
    val createdAt: Long,
    val updatedAt: Long
)