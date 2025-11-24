package top.writerpass.micromessage.server.core.data.service.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import top.writerpass.micromessage.server.core.data.service.user.data.UserDevice
import top.writerpass.micromessage.server.core.data.service.user.table.UserDeviceTable

class UserDeviceEntity(id: EntityID<Long>) : LongEntity(id){
    companion object: LongEntityClass<UserDeviceEntity>(UserDeviceTable)

    var userId by UserDeviceTable.userId
    var deviceType by UserDeviceTable.deviceType
    var model by UserDeviceTable.model
    var osVersion by UserDeviceTable.osVersion
    var appVersion by UserDeviceTable.appVersion
    var language by UserDeviceTable.language
    var pushToken by UserDeviceTable.pushToken
    var pushPlatform by UserDeviceTable.pushPlatform
    var ipAddress by UserDeviceTable.ipAddress
    var clientNetworkType by UserDeviceTable.clientNetworkType
    var isActive by UserDeviceTable.isActive
    var lastOnLineAt by UserDeviceTable.lastOnLineAt
    var lastHeartBeatAt by UserDeviceTable.lastHeartBeatAt
    var loginAt by UserDeviceTable.loginAt
    var logoutAt by UserDeviceTable.logoutAt


    fun toData(): UserDevice {
        return UserDevice(
            id = id.value,
            userId = userId.value,
            deviceType = deviceType,
            model = model,
            osVersion = osVersion,
            appVersion = appVersion,
            language = language,
            pushToken = pushToken,
            pushPlatform = pushPlatform,
            ipAddress = ipAddress,
            clientNetworkType = clientNetworkType,
            isActive = isActive,
            lastOnLineAt = lastOnLineAt,
            lastHeartBeatAt = lastHeartBeatAt,
            loginAt = loginAt,
            logoutAt = logoutAt
        )
    }
}