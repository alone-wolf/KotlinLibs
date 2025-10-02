package top.writerpass.rekuester

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.io.RandomAccessFile
import java.net.ServerSocket
import java.net.Socket
import java.nio.channels.FileChannel
import java.nio.channels.FileLock

class ApplicationSingletonHelper(
    private val appId: String,
    private val parentDir: String = System.getProperty("java.io.tmpdir"),
    private val lockFile: File = File(parentDir, "$appId-single_app.lock"),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    val boundPort: Int = -1,
) {

    companion object {
        private const val DEFAULT_START_PORT = 45678
        private const val MAX_PORT_TRY = 100
    }


    private var serverSocket: ServerSocket? = null

    private var fileChannel: FileChannel? = null
    private var fileLock: FileLock? = null

    suspend fun primaryInstanceAction(){
        // 尝试绑定端口并监听网络通信
        // 创建锁文件并对文件上锁
        // 将监听端口号保存到锁文件中
        // 返回true
    }

    suspend fun secondaryInstanceAction(){
        // 从锁文件中读取端口号
        // 尝试连接到该端口
        // 返回true
    }


    suspend fun tryLock(ipcHandler: suspend (String) -> Unit = {}): Boolean {
        if (lockFile.exists()){
            // 如果文件存在
            // 1.可能是脏环境
            // 2.或者单例运行中
            try {
                val fChannel = RandomAccessFile(lockFile, "rw").channel
                fChannel.tryLock().let { fLock ->
                    if (fLock == null) {
                        // 如果上锁失败，说明单例运行中
                        // 推出文件锁尝试步骤
                        // 从文件中读取port并执行ipc操作唤醒窗口
                        val port = lockFile.readText().trim().toInt()
                        fChannel.close()
                    }else{
                        // 如果上锁成功，说明单例未运行
                        // 这是脏环境
                        // 释放文件锁 + 删除文件 + 启动单实例
                        fLock.release()
                        fChannel?.close()
                        lockFile.delete()
                        startSingleInstance(ipcHandler)
                    }
                }
            }catch (_: Exception){

            }
        }else{
            return startSingleInstance(ipcHandler)
        }

        // 文件存在 → 尝试获取文件锁
        return withContext(Dispatchers.IO) {
            try {
                val fChannel = RandomAccessFile(lockFile, "rw").channel
                fChannel.tryLock().let { fLockNullable ->
                    if (fLockNullable == null) {
                        fChannel.close()
                        lockFile.delete()
                        startSingleInstance(ipcHandler)
                    }
                }

                if (fLock == null) {
                    // 脏环境，之前进程异常退出 → 释放锁 + 删除文件 + 重新启动单实例
                    fLock?.release()
                    fChannel?.close()
                    lockFile.delete()
                    startSingleInstance(ipcHandler)
                } else {
                    // 文件锁被占用 → 已有实例运行
                    val existingPort = readPortFromFile(lockFile)
                    if (existingPort != null) {
                        println("Another instance detected, sending IPC message to port $existingPort...")
                        sendIpcMessage(existingPort, "Hello from another instance")
                    }
                    false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // -------------------- 单例启动步骤 --------------------
    private suspend fun startSingleInstance(
        ipcHandler: (suspend (String) -> Unit)?
    ): Boolean = withContext(Dispatchers.IO) {
        // 尝试绑定端口
        var port = DEFAULT_START_PORT
        var success = false
        var server: ServerSocket? = null

        for (i in 0 until MAX_PORT_TRY) {
            try {
                server = ServerSocket(port)
                success = true
                boundPort = port
                break
            } catch (_: Exception) {
                port += 1
            }
        }

        if (!success || server == null) {
            println("Failed to bind any port in range $DEFAULT_START_PORT-${DEFAULT_START_PORT + MAX_PORT_TRY}")
            return@withContext false
        }

        serverSocket = server
        startIpcServer(ipcHandler)

        // 创建 lock 文件并写入端口号
        lockFile.createNewFile()
        lockFile.writeText(boundPort.toString())
        fileChannel = RandomAccessFile(lockFile, "rw").channel
        fileLock = fileChannel!!.lock() // 阻塞锁定

        println("Single instance started on port $boundPort")
        true
    }

    // -------------------- IPC 服务 --------------------
    private fun startIpcServer(ipcHandler: (suspend (String) -> Unit)?) {
        scope.launch {
            try {
                while (!serverSocket!!.isClosed) {
                    val client = serverSocket!!.accept()
                    launch {
                        client.use { socket ->
                            val msg = withContext(Dispatchers.IO) {
                                BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
                            }
                            if (msg != null && ipcHandler != null) {
                                ipcHandler(msg)
                            }
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    suspend fun sendIpcMessage(port: Int, message: String) = withContext(Dispatchers.IO) {
        try {
            Socket("127.0.0.1", port).use { socket ->
                PrintWriter(socket.getOutputStream(), true).use { out ->
                    out.println(message)
                }
            }
        } catch (_: Exception) {
        }
    }

    private suspend fun readPortFromFile(file: File): Int? = withContext(Dispatchers.IO) {
        try {
            file.readText().trim().toInt()
        } catch (_: Exception) {
            null
        }
    }

    fun release() {
        try {
            fileLock?.release()
        } catch (_: Exception) {
        }
        try {
            fileChannel?.close()
        } catch (_: Exception) {
        }
        try {
            serverSocket?.close()
        } catch (_: Exception) {
        }
        try {
            lockFile?.delete()
        } catch (_: Exception) {
        }
        scope.cancel()
    }

    fun getBoundPort(): Int = boundPort
}
