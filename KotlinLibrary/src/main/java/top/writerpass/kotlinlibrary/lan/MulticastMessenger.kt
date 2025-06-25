package top.writerpass.kotlinlibrary.lan

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import top.writerpass.kotlinlibrary.utils.println
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.net.SocketTimeoutException

class MulticastMessenger(
    private val groupAddress: String = "230.0.0.1",
    private val port: Int,
    private val tag: String? = null
) : LanSender, LanReceiver {

    override suspend fun send(message: String) = withContext(Dispatchers.IO) {
        val fullMessage = tag.orEmpty() + message
        MulticastSocket().use { socket ->
            val group = InetAddress.getByName(groupAddress)
            val buffer = fullMessage.toByteArray()
            val packet = DatagramPacket(buffer, buffer.size, group, port)
            socket.send(packet)
            println("[Multicast] Sent: '$fullMessage' to $group:$port")
        }
    }

    override suspend fun receive(
        timeoutMillis: Int?,
        filterTag: String?,
        onReceive: suspend (message: String, sender: InetAddress) -> Unit
    ) = withContext(Dispatchers.IO) {
        MulticastSocket(port).use { socket ->
            val group = InetAddress.getByName(groupAddress)
            socket.joinGroup(group)
            println("[Multicast] Listening on $group:$port")
            timeoutMillis?.let { socket.soTimeout = it }

            val buffer = ByteArray(1024)
            val packet = DatagramPacket(buffer, buffer.size)

            while (isActive) {
                try {
                    socket.receive(packet)
                    val raw = String(packet.data, 0, packet.length)

                    if (filterTag == null || raw.startsWith(filterTag)) {
                        val content = raw.removePrefix(filterTag.orEmpty())
                        onReceive(content, packet.address)
                    } else {
                        println("[Multicast] Ignored message without tag: $raw")
                    }
                } catch (e: SocketTimeoutException) {
                    println("[Multicast] Timeout")
                }
            }

            socket.leaveGroup(group)
        }
    }
}