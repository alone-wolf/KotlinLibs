package top.writerpass.ktorlibrary.fileslicesend.client

import top.writerpass.ktorlibrary.fileslicesend.common.InfoResponse
import java.io.File

sealed interface UploadSteps {
    object Idle : UploadSteps
    class Info(val file: File) : UploadSteps
    class Prepare(val file: File, val info: InfoResponse) :
        UploadSteps
    data class SendLoop(
        val file: File,
        val retryTimes: Int,
        val sliceFullSize: Long,
        val threadNum: Int,
        val subStep: UploadSubSteps,
        val uuid: String
    ) : UploadSteps

    class Finish(val success: Boolean) : UploadSteps
}