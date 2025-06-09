package top.writerpass.ktorlibrary.file_slice_send.common

import kotlinx.serialization.Serializable

/***
 * 获取服务器配置信息
 */
@Serializable
data class InfoResponse(
    val allowQuickSend: Boolean,
    val sliceThreshold: Long,
    val maxSliceSize: Long,
    val threadNum: Int,
)