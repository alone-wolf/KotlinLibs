package top.writerpass.kotlinlibrary.lan

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import top.writerpass.kotlinlibrary.utils.println
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import kotlin.collections.iterator

class BroadcastMessenger(
    private val port: Int,
    private val tag: String? = null,
    private val useGlobalBroadcast: Boolean = false
) : LanSender, LanReceiver {

    override suspend fun send(message: String) = withContext(Dispatchers.IO) {
        val fullMessage = tag.orEmpty() + message
        val broadcastAddress = if (useGlobalBroadcast) {
            InetAddress.getByName("255.255.255.255")
        } else {
            getLocalBroadcastAddress() ?: InetAddress.getByName("255.255.255.255")
        }

        DatagramSocket().use { socket ->
            socket.broadcast = true
            val buffer = fullMessage.toByteArray()
            val packet = DatagramPacket(buffer, buffer.size, broadcastAddress, port)
            socket.send(packet)
            println("[Broadcast] Sent: '$fullMessage' to $broadcastAddress:$port")
        }
    }

    override suspend fun receive(
        timeoutMillis: Int?,
        filterTag: String?,
        onReceive: suspend (message: String, sender: InetAddress) -> Unit
    ) = withContext(Dispatchers.IO) {
        DatagramSocket(port, InetAddress.getByName("0.0.0.0")).use { socket ->
            println("[Broadcast] Listening on port $port")
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
                        println("[Broadcast] Ignored message without tag: $raw")
                    }
                } catch (e: SocketTimeoutException) {
                    println("[Broadcast] Timeout")
                }
            }
        }
    }

    private fun getLocalBroadcastAddress(): InetAddress? {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (iFace in interfaces) {
            println("Interface: ${iFace.name}, up=${iFace.isUp}, loopback=${iFace.isLoopback}")
            if (!iFace.isUp || iFace.isLoopback) continue
            for (address in iFace.interfaceAddresses) {
                println("  Address: ${address.address}, Broadcast: ${address.broadcast}")
                val broadcast = address.broadcast
                if (address.address is Inet4Address && broadcast != null) {
                    return broadcast
                }
            }
        }
        return null
    }
}