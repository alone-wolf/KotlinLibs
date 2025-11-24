package top.writerpass.micromessage.server.core.data.service.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import top.writerpass.micromessage.server.core.data.service.user.data.UserSetting
import top.writerpass.micromessage.server.core.data.service.user.table.UserSettingTable

class UserSettingEntity(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<UserSettingEntity>(UserSettingTable)

    val userId by UserSettingTable.userId
    val allowAddByPhone by UserSettingTable.allowAddByPhone
    val allowAddByNickName by UserSettingTable.allowAddByNickName
    val allowAddByQRCode by UserSettingTable.allowAddByQRCode
    val allowTmpChat by UserSettingTable.allowTmpChat
    val allowGroupInvite by UserSettingTable.allowGroupInvite
    val showOnlineStatus by UserSettingTable.showOnlineStatus
    val showLastSeen by UserSettingTable.showLastSeen
    val showTypingIndicator by UserSettingTable.showTypingIndicator
    val sendReadReceipt by UserSettingTable.sendReadReceipt
    val muteAllNotification by UserSettingTable.muteAllNotification
    val notificationDetail by UserSettingTable.notificationDetail

    fun toData(): UserSetting {
        return UserSetting(
            userId = userId.value,
            allowAddByPhone = allowAddByPhone,
            allowAddByNickName = allowAddByNickName,
            allowAddByQRCode = allowAddByQRCode,
            allowTmpChat = allowTmpChat,
            allowGroupInvite = allowGroupInvite,
            showOnlineStatus = showOnlineStatus,
            showLastSeen = showLastSeen,
            showTypingIndicator = showTypingIndicator,
            sendReadReceipt = sendReadReceipt,
            muteAllNotifications = muteAllNotification,
            notificationDetail = notificationDetail
        )
    }
}