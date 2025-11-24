package top.writerpass.micromessage.server.core.data.service.user.data

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.server.core.data.base.BaseDataClass
import top.writerpass.micromessage.server.core.data.enums.NotificationDetailLevel

@Serializable
data class UserSetting(
    val userId: Long,
    val allowAddByPhone: Boolean,
    val allowAddByNickName: Boolean,
    val allowAddByQRCode: Boolean,
    val allowTmpChat: Boolean,
    val allowGroupInvite: Boolean,
    val showOnlineStatus: Boolean,
    val showLastSeen: Boolean,
    val showTypingIndicator: Boolean,
    val sendReadReceipt: Boolean,
    val muteAllNotifications: Boolean,
    val notificationDetail: NotificationDetailLevel
) : BaseDataClass {
    override val id: Long
        get() = userId
}

