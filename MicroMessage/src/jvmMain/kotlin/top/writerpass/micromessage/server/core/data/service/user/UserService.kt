package top.writerpass.micromessage.server.core.data.service.user

import top.writerpass.micromessage.server.core.data.base.BaseService
import top.writerpass.micromessage.server.core.data.base.dbQuery
import top.writerpass.micromessage.server.core.data.enums.CredentialType
import top.writerpass.micromessage.server.core.data.enums.IdentifierType
import top.writerpass.micromessage.server.core.data.service.auth.data.Credential
import top.writerpass.micromessage.server.core.data.service.user.entity.UserEntity
import top.writerpass.micromessage.server.core.data.service.user.entity.UserIdentifierEntity
import java.util.*

object UserService : BaseService {

    suspend fun registerNewUser(
        username: String,
        passwordHash0: String
    ) {
        val newSalt = UUID.randomUUID().toString()
        val passwordHash1 = passwordHash0 + newSalt

        dbQuery {
            val newUser = UserEntity.new {
                createdAt = 0L
            }.toData()
            val newUserId = newUser.id
            val newCreatedAt = newUser.createdAt

            val newUserIdentifier = UserIdentifierEntity.new {
                userId = newUserId
                type = IdentifierType.Username
                content = username
                enabled = true
                createdAt = newCreatedAt
                updatedAt = newCreatedAt
            }.toData()

            val newCredential = Credential.Entity.new {
                userId = newUserId
                identifierId = newUserIdentifier.id
                type = CredentialType.Password
                passwordHash = passwordHash1
                salt = newSalt
                createdAt = newCreatedAt
                updatedAt = newCreatedAt
            }.toData()
        }
    }
}