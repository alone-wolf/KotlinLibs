package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class DeviceType {
    Android,
    IOS,
    Windows,
    MacOS,
    Linux,
    Web,
    Unknown
}