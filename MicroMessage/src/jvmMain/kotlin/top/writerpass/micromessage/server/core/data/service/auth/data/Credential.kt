package top.writerpass.micromessage.server.core.data.service.auth.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass
import top.writerpass.micromessage.server.core.data.enums.CredentialType
import top.writerpass.micromessage.server.core.data.service.user.table.UserIdentifierTable
import top.writerpass.micromessage.server.core.data.service.user.table.UserTable

object Credential {
    @Serializable
    data class Data(
        override val id: Long, // PK
        val userId: Long, // FK User.id
        val identifierId: Long, // FK UserIdentifier.id
        val type: CredentialType = CredentialType.Password,
        val passwordHash: String,
        val salt: String,
        val createdAt: Long,
        val updatedAt: Long
    ) : BaseDataClass

    object CredentialTable : LongIdTable() {
        val userId = long("user_id").references(UserTable.id)
        val identifierId = long("identifier_id").references(UserIdentifierTable.id)
        val type = enumeration<CredentialType>("type")
        val passwordHash = varchar(
            name = "password_hash",
            length = 200
        )
        val salt = varchar(
            name = "salt",
            length = 100
        )
        val createdAt = long(name = "created_at")
        val updatedAt = long(name = "updated_at")
    }

    class Entity(id: EntityID<Long>) : LongEntity(id) {
        companion object : LongEntityClass<Entity>(CredentialTable)

        var userId by CredentialTable.userId
        var identifierId by CredentialTable.identifierId
        var type by CredentialTable.type
        var passwordHash by CredentialTable.passwordHash
        var salt by CredentialTable.salt
        var createdAt by CredentialTable.createdAt
        var updatedAt by CredentialTable.updatedAt

        fun toData(): Data {
            return Data(
                id = id.value,
                userId = userId,
                identifierId = identifierId,
                type = type,
                passwordHash = passwordHash,
                salt = salt,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}

