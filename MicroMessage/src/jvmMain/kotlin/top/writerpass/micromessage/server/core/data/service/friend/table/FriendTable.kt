package top.writerpass.micromessage.server.core.data.service.friend.table

import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.server.core.data.service.friend.data.FriendStatus
import top.writerpass.micromessage.server.core.data.service.user.table.UserTable

object FriendTable : LongIdTable() {
    val userId = reference("user_id", UserTable.id)
    val user1Id = reference("user1_id", UserTable.id)
    val status = enumeration<FriendStatus>("status")
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}