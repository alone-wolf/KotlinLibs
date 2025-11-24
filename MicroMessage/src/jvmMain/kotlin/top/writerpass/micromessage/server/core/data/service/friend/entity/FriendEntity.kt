package top.writerpass.micromessage.server.core.data.service.friend.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import top.writerpass.micromessage.server.core.data.service.friend.data.Friend
import top.writerpass.micromessage.server.core.data.service.friend.table.FriendTable

class FriendEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FriendEntity>(FriendTable)

    var userId by FriendTable.userId
    var user1Id by FriendTable.user1Id
    var status by FriendTable.status
    var createdAt by FriendTable.createdAt
    var updatedAt by FriendTable.updatedAt

    fun toData(): Friend {
        return Friend(
            id = id.value,
            userId = userId.value,
            user1Id = user1Id.value,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}