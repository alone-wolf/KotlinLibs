package top.writerpass.micromessage.server.core.data.service.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import top.writerpass.micromessage.server.core.data.service.user.data.UserIdentifier
import top.writerpass.micromessage.server.core.data.service.user.table.UserIdentifierTable

class UserIdentifierEntity(userId: EntityID<Long>) : LongEntity(userId) {
    companion object : LongEntityClass<UserIdentifierEntity>(UserIdentifierTable)

    var userId by UserIdentifierTable.userId
    var type by UserIdentifierTable.type
//    var method by UserIdentifierTable.method
    var content by UserIdentifierTable.content
    var enabled by UserIdentifierTable.enabled
    var createdAt by UserIdentifierTable.createdAt
    var updatedAt by UserIdentifierTable.updatedAt


    fun toData(): UserIdentifier {
        return UserIdentifier(
            id = id.value,
            userId = userId,
            type = type,
//            method = method,
            content = content,
            enabled = enabled,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}