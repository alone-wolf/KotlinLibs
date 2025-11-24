package top.writerpass.micromessage.server.core.data.service.auth.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass
import top.writerpass.micromessage.server.core.data.service.user.table.UserTable

object LoginSession{
    @Serializable
    data class Data(
        override val id: Long,
        val userId: Long,
        val sessionToken: String,
        val expiresAt: Long
    ): BaseDataClass

    object Table : LongIdTable() {
        val userId = reference(
            name = "user_id",
            refColumn = UserTable.id
        )
        val sessionToken = varchar(
            name = "session_token",
            length = 100
        ).uniqueIndex()
        val expiresAt = long("expires_at")
    }

    class Entity(id: EntityID<Long>) : LongEntity(id) {
        companion object : LongEntityClass<Entity>(Table)

        var userId by Table.userId
        var sessionToken by Table.sessionToken
        var expiresAt by Table.expiresAt

        fun toData(): Data {
            return Data(
                id = id.value,
                userId = userId.value,
                sessionToken = sessionToken,
                expiresAt = expiresAt
            )
        }
    }
}