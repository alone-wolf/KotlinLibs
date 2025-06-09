package top.writerpass.ktorlibrary.file_slice_send.client

// For Client
data class SliceState(
    val index: Int,
    val size: Int,
    val hash: String,
    val status: SliceUploadStatus,
    val errorMessage: String = "",
    val currentProcess: Long = 0
) {
    fun percentage(): String {
        return if (size > 0) {
            (currentProcess.toInt().toDouble() / size.toDouble() * 100).let {
                "%.2f".format(it)
            }
        } else {
            "0.00"
        }
    }

    fun hashing(): SliceState {
        return copy(status = SliceUploadStatus.Hashing)
    }

    fun sending(hash: String): SliceState {
        return copy(
            hash = hash,
            status = SliceUploadStatus.Sending
        )
    }

    fun sendingWithProgress(currentProcess: Long): SliceState {
        return copy(
            currentProcess = currentProcess
        )
    }

    fun sent(): SliceState {
        return copy(
            status = SliceUploadStatus.Sent
        )
    }

    fun error(error: String): SliceState {
        return copy(
            status = SliceUploadStatus.Error,
            errorMessage = error
        )
    }

    companion object {
        fun pending(index: Int, size: Int): SliceState {
            return SliceState(
                index = index,
                size = size,
                hash = "",
                status = SliceUploadStatus.Pending
            )
        }
    }
}