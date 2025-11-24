package top.writerpass.micromessage.server.core.data.service.user.data

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass
import top.writerpass.micromessage.server.core.data.enums.NetworkType
import top.writerpass.micromessage.server.core.data.enums.OnLineStatus

@Serializable
data class UserStatus(
    val userId: Long,
    val onStatus: OnLineStatus,
    val lastOnLineAt: Long,
    val currentDeviceId: Long,
    val currentIp: String,
    val currentNetworkType: NetworkType,
    val heartBeatAt: Long,
) : BaseDataClass {
    override val id: Long
        get() = userId
}

