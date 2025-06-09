package top.writerpass.ktorlibrary.file_slice_send.client

sealed interface UploadSubSteps {
    class Send() : UploadSubSteps
    class Status() : UploadSubSteps
}