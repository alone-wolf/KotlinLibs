package top.writerpass.ktorlibrary.fileslicesend.common

import kotlinx.serialization.Serializable

@Serializable
class TrunkState(
    val index: Int,
    val size: Long,
    val hash: String?,
    val status: TrunkStatus
)