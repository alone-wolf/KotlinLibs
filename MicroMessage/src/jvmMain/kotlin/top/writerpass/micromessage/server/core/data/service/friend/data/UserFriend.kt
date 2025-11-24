package top.writerpass.micromessage.server.core.data.service.friend.data

import kotlinx.serialization.Serializable

@Serializable
data class UserFriend(
    val id: Long,
    val ownerId: Long,
    val targetId: Long,
    val remark: String,
    val tags: List<String>,
    val isMuted: Boolean,
    val isBlocked: Boolean,
    val isStarred: Boolean,
)