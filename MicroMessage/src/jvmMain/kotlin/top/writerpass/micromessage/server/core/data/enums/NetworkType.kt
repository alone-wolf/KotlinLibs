package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class NetworkType {
    Wire, Wifi, Mobile5G, Mobile4G, Mobile3G, Mobile2G, Unknown
}