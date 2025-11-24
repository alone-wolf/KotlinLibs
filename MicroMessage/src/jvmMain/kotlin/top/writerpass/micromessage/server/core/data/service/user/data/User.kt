package top.writerpass.micromessage.server.core.data.service.user.data

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass

@Serializable
data class User(
    override val id: Long,
    val createdAt: Long
): BaseDataClass
