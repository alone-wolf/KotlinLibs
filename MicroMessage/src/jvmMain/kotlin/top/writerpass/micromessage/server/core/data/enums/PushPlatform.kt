package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class
PushPlatform {
    APNS, FCM, HMS, WEBSOCKET
}