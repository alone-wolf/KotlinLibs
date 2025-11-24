package top.writerpass.micromessage.server.core.data.service.user.data

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass
import top.writerpass.micromessage.server.core.data.enums.DeviceType
import top.writerpass.micromessage.server.core.data.enums.Language
import top.writerpass.micromessage.server.core.data.enums.NetworkType
import top.writerpass.micromessage.server.core.data.enums.PushPlatform

@Serializable  // TODO: 根据不同的DeviceType编写不同的creator
data class UserDevice(
    override val id: Long, // PK

    val userId: Long, // FK User.id

    val deviceType: DeviceType,
    val model: String, // default: unknown
    val osVersion: String, // default: unknown
    val appVersion: String, // default: unknown
    val language: Language,

    val pushToken: String?, // default: null
    val pushPlatform: PushPlatform,

    val ipAddress: String,
    val clientNetworkType: NetworkType,

    val isActive: Boolean,
    val lastOnLineAt: Long,
    val lastHeartBeatAt: Long,

    val loginAt: Long,
    val logoutAt: Long,
) : BaseDataClass


