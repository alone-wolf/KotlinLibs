package top.writerpass.kotlinlibrary.lan

interface LanSender {
    suspend fun send(message: String)
}