package top.writerpass.micromessage.server.core.data.service.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import top.writerpass.micromessage.server.core.data.service.user.data.User
import top.writerpass.micromessage.server.core.data.service.user.table.UserTable

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(
        UserTable
    )

    var createdAt by UserTable.createdAt

    fun toData(): User {
        return User(
            id = id.value,
            createdAt = createdAt
        )
    }
}
