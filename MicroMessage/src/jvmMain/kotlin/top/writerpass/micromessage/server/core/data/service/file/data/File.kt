package top.writerpass.micromessage.server.core.data.service.file.data

import kotlinx.serialization.Serializable


// 客户端先计算md5
// 如果md5不存在，则上传
// 如果md5存在，则计算sha1
// 如果sha1不存在，则上传
// 如果sha1存在，则计算sha256
// 如果sha256不存在，则上传
// 否则sha256存在，则跳过上传
@Serializable
data class File(
    val id: Long,
    val name: String,
    val size: Long,
    val mimeType: String,
    val token: String,
    val createdAt: Long,
    val isDeleted: Boolean,
    val deletedAt: Long?,
    val md5: String,
    val sha1: String,
    val shA256: String,
    val refCount: String,
)
