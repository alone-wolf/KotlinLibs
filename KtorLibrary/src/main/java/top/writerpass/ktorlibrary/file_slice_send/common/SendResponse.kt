package top.writerpass.ktorlibrary.file_slice_send.common

import kotlinx.serialization.Serializable

@Serializable
class SendResponse(
    val uuid: String,
    val success: Boolean
)