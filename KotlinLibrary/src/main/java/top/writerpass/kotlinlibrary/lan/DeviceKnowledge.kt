package top.writerpass.kotlinlibrary.lan

import kotlinx.serialization.Serializable
import top.writerpass.kotlinlibrary.json

@Serializable
data class DeviceKnowledge(
    val ip: String,
    val port: Int,
    val name: String,
    val type: String = "PC"
) {
    fun toJsonString(): String {
        return json.encodeToString(this)
    }
}