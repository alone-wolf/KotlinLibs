package top.writerpass.ktorlibrary.file_slice_send.common

import kotlinx.serialization.Serializable

@Serializable
data class PrepareResponse(
    val uuid: String,
    val quickSendMatch: Boolean
)