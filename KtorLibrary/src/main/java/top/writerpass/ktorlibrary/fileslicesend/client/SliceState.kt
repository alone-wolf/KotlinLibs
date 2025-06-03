package top.writerpass.ktorlibrary.fileslicesend.client

// For Client
data class SliceState(
    val index: Int,
    val size: Int,
    val hash: String,
    val status: SliceUploadStatus,
    val errorMessage: String = ""
) {
    fun hashing(): SliceState {
        return copy(status = SliceUploadStatus.Hashing)
    }

    fun sending(hash: String): SliceState {
        return copy(
            hash = hash,
            status = SliceUploadStatus.Sending
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