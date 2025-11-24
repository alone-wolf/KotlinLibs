package top.writerpass.micromessage.server.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class IdentifierType {
    Username, // single, required
    Email, // multiple, optional
    Phone // multiple, optional
}