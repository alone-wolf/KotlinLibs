package top.writerpass.micromessage.server.core.data.service.user.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import top.writerpass.micromessage.server.core.data.enums.NotificationDetailLevel

object UserSettingTable : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = reference(
        name = "user_id",
        refColumn = UserTable.id,
    ).uniqueIndex()

    val userId = id

    val allowAddByPhone = bool("allow_add_by_phone")
    val allowAddByNickName = bool("allow_add_by_nickname")
    val allowAddByQRCode = bool("allow_add_by_qrcode")
    val allowTmpChat = bool("allow_tmp_chat")
    val allowGroupInvite = bool("allow_group_invite")
    val showOnlineStatus = bool("show_online_status")
    val showLastSeen = bool("show_last_seen")
    val showTypingIndicator = bool("show_typing_indicator")
    val sendReadReceipt = bool("send_read_receipt")
    val muteAllNotification = bool("mute_all_notification")
    val notificationDetail = enumeration<NotificationDetailLevel>("notification_detail")
}