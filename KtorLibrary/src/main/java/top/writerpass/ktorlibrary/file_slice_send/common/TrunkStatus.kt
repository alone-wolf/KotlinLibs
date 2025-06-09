package top.writerpass.ktorlibrary.file_slice_send.common

import kotlinx.serialization.Serializable

@Serializable
enum class TrunkStatus(val value: Int) {
    Pending(0),
    Sending(1),
    Checking(2),
    ReSendNeeded(3),
    Finished(4);
}