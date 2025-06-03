package top.writerpass.ktorlibrary.fileslicesend.common

import kotlinx.serialization.Serializable

@Serializable
data class PrepareResponse(
    val uuid: String,
    val quickSendMatch: Boolean
)