package top.writerpass.micromessage.server.core.data.service.user.table

import org.jetbrains.exposed.dao.id.LongIdTable
//import top.writerpass.micromessage.core.data.service.user.data.CredentialMethod
import top.writerpass.micromessage.server.core.data.enums.IdentifierType

object UserIdentifierTable : LongIdTable() {
    val userId = long("user_id").references(UserTable.id)

    val type = enumeration<IdentifierType>("type")
//    val method = enumeration<CredentialMethod>("method")
    val content = varchar(
        name = "content",
        length = 100
    )

    val enabled = bool("enabled")

    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}