package top.writerpass.ktorlibrary.file_slice_send.server

import kotlinx.serialization.Serializable

//@Serializable
//data class FileUploadPreparation(
//    val name: String,
//    val size: Long,
//    val hash: String?,
//    val fragmentSize: Long,
//    val fragmentNum: Int,
//    val lastFragmentSize: Long,
//    val quickSend: Boolean
//)

//data class FileUploadSend(
//    val sessionId: String,
//    val fragmentId: Int,
//    val fragmentSize: Long,
//    val file: File
//)

class FileSaved(
    val id: String,
    val name: String,
    val size: Long,
    val hash: String,
)

@Serializable
class UploadCache(
    val sessionId: String,
    val name: String,
    val size: Long,
    val hash: String?,
    val fragmentFullNum: Int,
    val fragmentFullSize: Long,
    val fragmentLastSize: Long
) {
    val fragmentStatus: MutableMap<Int, FragmentStatus> = mutableMapOf()
}

@Serializable
class FragmentStatus(
    val id: Int,
    val clientHash: String,
    val serverHash: String,
    val state: FragmentState
)

@Serializable
enum class FragmentState { Waiting, Uploading, UploadError, Uploaded, Finished }