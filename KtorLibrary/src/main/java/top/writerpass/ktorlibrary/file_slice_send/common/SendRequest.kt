﻿package top.writerpass.ktorlibrary.file_slice_send.common

import kotlinx.serialization.Serializable

@Serializable
class SendRequest(
    val uuid: String,
    val index: Int,
    val data: ByteArray,
    val resend: Boolean = false,
    val resendTime: Int = 0
)