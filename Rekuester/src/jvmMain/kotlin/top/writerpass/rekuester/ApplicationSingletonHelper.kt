package top.writerpass.rekuester

import kotlinx.coroutines.runBlocking
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.kmplibrary.utils.println
import top.writerpass.kmplibrary.utils.switch
import java.io.File
import java.io.RandomAccessFile
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

fun File.switchExists(
    onExists: (File) -> Unit,
    onNotExists: (File) -> Unit
) {
    if (exists()) {
        onExists(this)
    } else {
        onNotExists(this)
    }
}

abstract class Helper(
    val appIdentifier: String,
    private val parentDir: String = System.getProperty("java.io.tmpdir")
) {
    private val HOST = "127.0.0.1"

    private fun getLockFile(): File {
        return File(parentDir, "$appIdentifier-single_app.lock")
    }

    fun checkLock() {
        "Helper::checkLock".println()
        val lockFile = getLockFile()
        lockFile.switchExists(
            onNotExists = { primaryInstanceAction(lockFile) },
            onExists = { tryLock(lockFile) }
        )
    }

    private fun tryLock(lockFile: File) {
        val raFile = RandomAccessFile(lockFile, "rw")
        val fChannel = raFile.channel
        val fLock = fChannel.tryLock()
        fLock.switch(
            isNull = {
                fChannel.close()
                raFile.close()
                lockFile.delete()
                secondaryInstanceAction(lockFile)
            },
            isNotNull = {
                fLock.release()
                fChannel.close()
                raFile.close()
                lockFile.delete()
                primaryInstanceAction(lockFile)
            }
        )
    }

    private fun primaryInstanceAction(lockFile: File) {
        "Helper::primaryInstanceAction".println()
        // 尝试启动IPC服务
        tryBindPorts().switch(
            isNull = {
                "UDP Server Start Failed, no port available, exiting".println()
            },
            isNotNull = { (socket, port) ->
                lockFile.deleteOnExit()
                lockFile.createNewFile()
                lockFile.writeText(port.toString())
                RandomAccessFile(lockFile, "rw").use { raFile ->
                    raFile.channel.use { fChannel ->
                        runCatching {
                            fChannel.tryLock()
                        }.onSuccess { fLock ->
                            "UPD Server is Running, listening to ${HOST}:${port}".println()
                            val buffer = ByteArray(1024) // 接收缓冲区
                            while (true) {
                                val packet = DatagramPacket(buffer, buffer.size)
                                socket.receive(packet) // 阻塞等待客户端消息

                                val received = String(packet.data, 0, packet.length)
                                val clientAddress = packet.address.hostAddress
                                val clientPort = packet.port

                                println("收到来自 $clientAddress:$clientPort 的消息: $received")
                            }
//                            fLock.release()
                        }.onFailure { t ->
                            t.localizedMessage.println()
                            "LockFile Lock Failed with no more actions, exiting".println()
                        }
                    }
                }
            }
        )
        // 将端口号写到lock文件中
        // 将文件上锁
    }

    private fun tryBindPorts(): Pair<DatagramSocket, Int>? {
        "Helper::tryBindPorts".println()
        for (port in (1000..65535).toList()) {
            try {
                val socket = DatagramSocket(port, InetAddress.getByName(HOST))
                return socket to port
            } catch (e: SocketException) {
                println("端口 $port 被占用，尝试下一个..., e:${e.localizedMessage}")
            }
        }
        return null
    }

    private fun secondaryInstanceAction(lockFile: File) {
        "Helper::secondaryInstanceAction".println()
        // 从lock文件中读取端口号
        // 尝试连接到该端口并发送请求
        val port = lockFile.readText().trim().toInt()
        ipcClient(port)
    }

    private fun ipcClient(port: Int) {
        "Helper::ipcClient".println()
        runCatching {
            val message = "Hello, UDP!"
            val host = "127.0.0.1"   // 目标地址，可以换成对方的IP

            DatagramSocket().use { socket ->
                val buffer = message.toByteArray()
                val address = InetAddress.getByName(host)
                val packet = DatagramPacket(
                    buffer,
                    buffer.size,
                    address,
                    port
                )

                socket.send(packet)
                println("已发送: \"$message\" -> $host:$port")
            }
        }
    }
}

fun main() {
    runBlocking {
        val helper = object : Helper("top.writerpass.rekuester") {}
        launchIO {
            helper.checkLock()
        }
    }
}

//class ApplicationSingletonHelper(
//    private val appId: String,
//    private val parentDir: String = System.getProperty("java.io.tmpdir"),
//    private val lockFile: File = File(parentDir, "$appId-single_app.lock"),
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//    val boundPort: Int = -1,
//) {
//
//    companion object {
//        private const val DEFAULT_START_PORT = 45678
//        private const val MAX_PORT_TRY = 100
//    }
//
//
//    private var serverSocket: ServerSocket? = null
//
//    private var fileChannel: FileChannel? = null
//    private var fileLock: FileLock? = null
//
//    suspend fun primaryInstanceAction() {
//        // 尝试绑定端口并监听网络通信
//        // 创建锁文件并对文件上锁
//        // 将监听端口号保存到锁文件中
//        // 返回true
//    }
//
//    suspend fun secondaryInstanceAction() {
//        // 从锁文件中读取端口号
//        // 尝试连接到该端口
//        // 返回true
//    }
//
//
//    suspend fun tryLock(ipcHandler: suspend (String) -> Unit = {}): Boolean {
//        if (lockFile.exists()) {
//            // 如果文件存在
//            // 1.可能是脏环境
//            // 2.或者单例运行中
//            try {
//                val fChannel = RandomAccessFile(lockFile, "rw").channel
//                fChannel.tryLock().let { fLock ->
//                    if (fLock == null) {
//                        // 如果上锁失败，说明单例运行中
//                        // 推出文件锁尝试步骤
//                        // 从文件中读取port并执行ipc操作唤醒窗口
//                        val port = lockFile.readText().trim().toInt()
//                        fChannel.close()
//                    } else {
//                        // 如果上锁成功，说明单例未运行
//                        // 这是脏环境
//                        // 释放文件锁 + 删除文件 + 启动单实例
//                        fLock.release()
//                        fChannel?.close()
//                        lockFile.delete()
//                        startSingleInstance(ipcHandler)
//                    }
//                }
//            } catch (_: Exception) {
//
//            }
//        } else {
//            return startSingleInstance(ipcHandler)
//        }
//
//        // 文件存在 → 尝试获取文件锁
//        return withContext(Dispatchers.IO) {
//            try {
//                val fChannel = RandomAccessFile(lockFile, "rw").channel
//                fChannel.tryLock().let { fLockNullable ->
//                    if (fLockNullable == null) {
//                        fChannel.close()
//                        lockFile.delete()
//                        startSingleInstance(ipcHandler)
//                    }
//                }
//
//                if (fLock == null) {
//                    // 脏环境，之前进程异常退出 → 释放锁 + 删除文件 + 重新启动单实例
//                    fLock?.release()
//                    fChannel?.close()
//                    lockFile.delete()
//                    startSingleInstance(ipcHandler)
//                } else {
//                    // 文件锁被占用 → 已有实例运行
//                    val existingPort = readPortFromFile(lockFile)
//                    if (existingPort != null) {
//                        println("Another instance detected, sending IPC message to port $existingPort...")
//                        sendIpcMessage(existingPort, "Hello from another instance")
//                    }
//                    false
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                false
//            }
//        }
//    }
//
//    // -------------------- 单例启动步骤 --------------------
//    private suspend fun startSingleInstance(
//        ipcHandler: (suspend (String) -> Unit)?
//    ): Boolean = withContext(Dispatchers.IO) {
//        // 尝试绑定端口
//        var port = DEFAULT_START_PORT
//        var success = false
//        var server: ServerSocket? = null
//
//        for (i in 0 until MAX_PORT_TRY) {
//            try {
//                server = ServerSocket(port)
//                success = true
//                boundPort = port
//                break
//            } catch (_: Exception) {
//                port += 1
//            }
//        }
//
//        if (!success || server == null) {
//            println("Failed to bind any port in range $DEFAULT_START_PORT-${DEFAULT_START_PORT + MAX_PORT_TRY}")
//            return@withContext false
//        }
//
//        serverSocket = server
//        startIpcServer(ipcHandler)
//
//        // 创建 lock 文件并写入端口号
//        lockFile.createNewFile()
//        lockFile.writeText(boundPort.toString())
//        fileChannel = RandomAccessFile(lockFile, "rw").channel
//        fileLock = fileChannel!!.lock() // 阻塞锁定
//
//        println("Single instance started on port $boundPort")
//        true
//    }
//
//    // -------------------- IPC 服务 --------------------
//    private fun startIpcServer(ipcHandler: (suspend (String) -> Unit)?) {
//        scope.launch {
//            try {
//                while (!serverSocket!!.isClosed) {
//                    val client = serverSocket!!.accept()
//                    launch {
//                        client.use { socket ->
//                            val msg = withContext(Dispatchers.IO) {
//                                BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
//                            }
//                            if (msg != null && ipcHandler != null) {
//                                ipcHandler(msg)
//                            }
//                        }
//                    }
//                }
//            } catch (_: Exception) {
//            }
//        }
//    }
//
//    suspend fun sendIpcMessage(port: Int, message: String) = withContext(Dispatchers.IO) {
//        try {
//            Socket("127.0.0.1", port).use { socket ->
//                PrintWriter(socket.getOutputStream(), true).use { out ->
//                    out.println(message)
//                }
//            }
//        } catch (_: Exception) {
//        }
//    }
//
//    private suspend fun readPortFromFile(file: File): Int? = withContext(Dispatchers.IO) {
//        try {
//            file.readText().trim().toInt()
//        } catch (_: Exception) {
//            null
//        }
//    }
//
//    fun release() {
//        try {
//            fileLock?.release()
//        } catch (_: Exception) {
//        }
//        try {
//            fileChannel?.close()
//        } catch (_: Exception) {
//        }
//        try {
//            serverSocket?.close()
//        } catch (_: Exception) {
//        }
//        try {
//            lockFile?.delete()
//        } catch (_: Exception) {
//        }
//        scope.cancel()
//    }
//
//    fun getBoundPort(): Int = boundPort
//}
