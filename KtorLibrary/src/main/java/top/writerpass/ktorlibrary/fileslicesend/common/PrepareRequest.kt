package top.writerpass.ktorlibrary.fileslicesend.common

import kotlinx.serialization.Serializable

/***
 * 上传前准备，携带文件相关信息
 */
@Serializable
data class PrepareRequest(
    val filename: String,
    val size: Long,
    val quickSend: Boolean,
    val hash: String?,
    val fragmentFullNum: Int,
    val fragmentFullSize: Long,
    val fragmentLastSize: Long
)