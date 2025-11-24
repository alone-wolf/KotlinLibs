package top.writerpass.micromessage.server.core.data.service.user.table

import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.server.core.data.enums.DeviceType
import top.writerpass.micromessage.server.core.data.enums.Language
import top.writerpass.micromessage.server.core.data.enums.NetworkType
import top.writerpass.micromessage.server.core.data.enums.PushPlatform

object UserDeviceTable : LongIdTable() {
    val userId = reference(
        name = "user_id",
        refColumn = UserTable.id
    )

    val deviceType = enumeration<DeviceType>("device_type")
    val model = varchar(
        name = "model",
        length = 100
    ).default("unknown")
    val osVersion = varchar(
        name = "os_version",
        length = 100
    ).default("unknown")
    val appVersion = varchar(
        name = "app_version",
        length = 100
    ).default("unknown")
    val language = enumeration<Language>("language")

    val pushToken = varchar(
        name = "push_token",
        length = 100
    ).nullable()
    val pushPlatform = enumeration<PushPlatform>("push_platform")

    val ipAddress = varchar(
        name = "ip_address",
        length = 100
    )
    val clientNetworkType = enumeration<NetworkType>("client_network_type")

    val isActive = bool("is_active").default(true)
    val lastOnLineAt = long("last_on_line_at")
    val lastHeartBeatAt = long("last_heart_beat_at")

    val loginAt = long("login_at")
    val logoutAt = long("logout_at")
}