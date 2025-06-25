package top.writerpass.kotlinlibrary.lan

import java.net.InetAddress

interface LanReceiver {
    suspend fun receive(
        timeoutMillis: Int? = null,
        filterTag: String? = null,
        onReceive: suspend (message: String, sender: InetAddress) -> Unit
    )
}