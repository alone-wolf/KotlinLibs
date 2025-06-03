package top.writerpass.ktorlibrary.fileslicesend.common

import kotlinx.serialization.Serializable

@Serializable
class StatusResponse(
    val uuid: String,
    val trunkState: List<TrunkState>
)