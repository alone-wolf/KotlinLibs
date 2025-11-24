package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Gender {
    Male,
    Female,
    Secret
}