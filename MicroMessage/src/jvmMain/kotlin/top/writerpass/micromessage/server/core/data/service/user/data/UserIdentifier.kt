package top.writerpass.micromessage.server.core.data.service.user.data

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass
import top.writerpass.micromessage.server.core.data.enums.IdentifierType


@Serializable
data class UserIdentifier(
    override val id: Long, // PK
    val userId: Long, // FK User.id
    val type: IdentifierType,
//    val method: CredentialMethod,
    val content: String,
    val enabled: Boolean,
    val createdAt: Long,
    val updatedAt: Long
) : BaseDataClass

//@Serializable
//enum class CredentialMethod {
//    PasswordOnly,
//    CodeOnly,
//    Both
//}


