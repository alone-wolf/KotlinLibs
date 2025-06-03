package top.writerpass.ktorlibrary.fileslicesend.server

import kotlinx.serialization.Serializable

//object FileTransmitSdk{
//    /***
//     * 获取服务器配置信息
//     */
//    @Serializable
//    data class InfoResponse(
//        val allowQuickSend: Boolean,
//        val fragmentThreshold: Long,
//        val maxFragmentSize: Long,
//        val threadNum:Int,
//    )
//
//    /***
//     * 上传前准备，携带文件相关信息
//     */
//    @Serializable
//    data class PrepareRequest(
//        val filename: String,
//        val size: Long,
//        val quickSend: Boolean,
//        val hash: String?,
//        val fragmentFullNum: Int,
//        val fragmentFullSize: Long,
//        val fragmentLastSize: Long
//    )
//
//    @Serializable
//    data class PrepareResponse(
//        val uuid: String,
//        val quickSendMatch: Boolean
//    )
//
//    @Serializable
//    class SendRequest(
//        val uuid: String,
//        val index: Int,
//        val data: ByteArray,
//        val resend: Boolean = false,
//        val resendTime: Int = 0
//    )
//
//    @Serializable
//    class SendResponse(
//        val uuid: String,
//        val success: Boolean
//    )
//
//    @Serializable
//    class StatusResponse(
//        val uuid: String,
//        val trunkState: List<TrunkState>
//    )
//
//    @Serializable
//    class TrunkState(
//        val index: Int,
//        val size: Long,
//        val hash: String?,
//        val status: TrunkStatus
//    )
//
//    @Serializable
//    enum class TrunkStatus(val value: Int) {
//        Pending(0),
//        Sending(1),
//        Checking(2),
//        ReSendNeeded(3),
//        Finished(4);
//    }
//}