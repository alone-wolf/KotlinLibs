package top.writerpass.ktorlibrary.fileslicesend.client

sealed interface UploadSubSteps {
    class Send() : UploadSubSteps
    class Status() : UploadSubSteps
}