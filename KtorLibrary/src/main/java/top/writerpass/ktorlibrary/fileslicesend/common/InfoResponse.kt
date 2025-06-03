package top.writerpass.ktorlibrary.fileslicesend.common

import kotlinx.serialization.Serializable

/***
 * 获取服务器配置信息
 */
@Serializable
data class InfoResponse(
    val allowQuickSend: Boolean,
    val fragmentThreshold: Long,
    val maxFragmentSize: Long,
    val threadNum: Int,
)