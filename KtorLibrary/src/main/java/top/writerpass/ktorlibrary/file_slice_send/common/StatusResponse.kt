package top.writerpass.ktorlibrary.file_slice_send.common

import kotlinx.serialization.Serializable

@Serializable
class StatusResponse(
    val uuid: String,
    val trunkState: List<TrunkState>
)