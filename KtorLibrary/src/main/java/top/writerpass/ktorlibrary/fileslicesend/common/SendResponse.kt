package top.writerpass.ktorlibrary.fileslicesend.common

import kotlinx.serialization.Serializable

@Serializable
class SendResponse(
    val uuid: String,
    val success: Boolean
)