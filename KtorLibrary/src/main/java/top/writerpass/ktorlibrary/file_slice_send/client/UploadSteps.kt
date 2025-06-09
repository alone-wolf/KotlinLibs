package top.writerpass.ktorlibrary.file_slice_send.client

import top.writerpass.ktorlibrary.file_slice_send.common.InfoResponse
import java.io.File

sealed interface UploadSteps {

    object Idle : UploadSteps

    class Info() : UploadSteps

    class Prepare(val file: File, val info: InfoResponse):UploadSteps

    data class SendLoop(
        val retryTimes: Int,
        val sliceFullSize: Long,
        val threadNum: Int,
        val subStep: UploadSubSteps,
        val uuid: String
    ):UploadSteps

    class Finish(val success: Boolean):UploadSteps
}