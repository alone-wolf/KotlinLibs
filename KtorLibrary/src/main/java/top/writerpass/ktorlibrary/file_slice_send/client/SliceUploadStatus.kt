package top.writerpass.ktorlibrary.file_slice_send.client

enum class SliceUploadStatus {
    Pending, Hashing, Sending, Sent, Error, Successful
}