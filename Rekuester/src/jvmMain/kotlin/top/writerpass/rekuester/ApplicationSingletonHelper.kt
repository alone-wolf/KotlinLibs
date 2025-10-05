//package top.writerpass.rekuester
//
//import kotlinx.coroutines.runBlocking
//import top.writerpass.kmplibrary.coroutine.launchIO
//import top.writerpass.kmplibrary.utils.println
//import java.io.File
//import java.io.RandomAccessFile
//import java.net.DatagramPacket
//import java.net.DatagramSocket
//import java.net.InetAddress
//import java.net.SocketException
//import java.nio.channels.FileLock
//
//fun File.switchExists(
//    onExists: (File) -> Unit,
//    onNotExists: (File) -> Unit
//) {
//    if (exists()) onExists(this) else onNotExists(this)
//}
//
//abstract class ApplicationSingletonHelper(
//    val appIdentifier: String,
//    private val parentDir: String = System.getProperty("java.io.tmpdir")
//) {
//    private val HOST = "127.0.0.1"
//
//    private fun getLockFile(): File =
//        File(parentDir, "$appIdentifier-single_app.lock")
//
//    fun applicationSingleton() {
//        "Helper::applicationSingleton".println()
//        val lockFile = getLockFile()
//        lockFile.switchExists(
//            onNotExists = { primaryInstanceAction(it) },
//            onExists = { tryLock(it) }
//        )
//    }
//
//    /**
//     * 尝试对 lock 文件加锁
//     * 如果能加锁，说明旧文件遗留，当前成为主实例；
//     * 如果不能加锁，说明已有主实例，执行 secondary 动作。
//     */
//    private fun tryLock(lockFile: File) {
//        "Helper::tryLock".println()
//        RandomAccessFile(lockFile, "rw").use { raFile ->
//            val channel = raFile.channel
//            val lock = runCatching { channel.tryLock() }.getOrNull()
//            if (lock == null) {
//                "Lock acquired by another process, becoming secondary".println()
//                secondaryInstanceAction(lockFile)
//            } else {
//                "Stale lock file found, becoming primary".println()
//                primaryInstanceAction(lockFile, lock)
//            }
//        }
//    }
//
//    /**
//     * 主实例逻辑：
//     *  - 绑定 UDP 端口
//     *  - 将端口写入 lock 文件
//     *  - 持续监听消息
//     */
//    private fun primaryInstanceAction(lockFile: File, fLock: FileLock? = null) {
//        "Helper::primaryInstanceAction".println()
//        val portPair = tryBindPorts()
//        if (portPair == null) {
//            "No available port found, exiting".println()
//            fLock?.release()
//            return
//        }
//
//        val (socket, port) = portPair
//        "UDP Server running on $HOST:$port".println()
//
//        // 写入端口号到锁文件
//        lockFile.writeText(port.toString())
//
//        // 注册退出时清理逻辑
//        Runtime.getRuntime().addShutdownHook(Thread {
//            "Shutdown hook triggered, releasing lock and closing socket".println()
//            runCatching {
//                socket.close()
//                fLock?.release()
//            }
//        })
//
//        // 异步监听 UDP 消息
//        val buffer = ByteArray(1024)
//        while (true) {
//            try {
//                val packet = DatagramPacket(buffer, buffer.size)
//                socket.receive(packet) // 阻塞等待客户端消息
//
//                val received = String(packet.data, 0, packet.length)
//                val clientAddr = "${packet.address.hostAddress}:${packet.port}"
//                println("收到来自 $clientAddr 的消息: $received")
//
//                // 可扩展响应逻辑
//                onMessageReceived(socket, packet, received)
//            } catch (e: Exception) {
//                if (socket.isClosed) break
//                "UDP receive error: ${e.localizedMessage}".println()
//            }
//        }
//    }
//
//    /**
//     * 从实例逻辑：
//     * 读取主实例端口号，尝试发送 UDP 消息。
//     */
//    private fun secondaryInstanceAction(lockFile: File) {
//        "Helper::secondaryInstanceAction".println()
//        val port = runCatching { lockFile.readText().trim().toInt() }.getOrElse {
//            "Failed to read port from lock file, fallback to primary".println()
//            primaryInstanceAction(lockFile)
//            return
//        }
//        ipcClient(port)
//    }
//
//    private fun ipcClient(port: Int) {
//        "Helper::ipcClient".println()
//        val message = "Hello from secondary instance!"
//        val host = InetAddress.getByName(HOST)
//
//        DatagramSocket().use { socket ->
//            val buffer = message.toByteArray()
//            val packet = DatagramPacket(buffer, buffer.size, host, port)
//            socket.send(packet)
//            println("已发送: \"$message\" -> $HOST:$port")
//        }
//    }
//
//    private fun tryBindPorts(): Pair<DatagramSocket, Int>? {
//        "Helper::tryBindPorts".println()
//        for (port in 10_000..65_535) {
//            try {
//                val socket = DatagramSocket(port, InetAddress.getByName(HOST))
//                socket.soTimeout = 0
//                return socket to port
//            } catch (_: SocketException) {
//                // 被占用，继续尝试
//            }
//        }
//        return null
//    }
//
//    /**
//     * 可由子类重写的钩子：主实例接收到消息时回调
//     */
//    open fun onMessageReceived(socket: DatagramSocket, packet: DatagramPacket, message: String) {}
//}
//
//fun main() {
//    runBlocking {
//        val helper = object : ApplicationSingletonHelper("top.writerpass.rekuester") {}
//        launchIO {
//            helper.applicationSingleton()
//        }
//    }
//}
