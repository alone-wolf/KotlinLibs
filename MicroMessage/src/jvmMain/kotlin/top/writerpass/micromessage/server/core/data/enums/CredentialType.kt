package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class CredentialType {
    Password, // passwordHash, salt
}