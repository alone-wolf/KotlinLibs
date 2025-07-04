package top.writerpass.kotlinlibrary.shell

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

object ShellExecutor {

    fun executeAsync(
        command: List<String>,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
        onStdout: (String) -> Unit = {},
        onStderr: (String) -> Unit = {}
    ): Job {
        return coroutineScope.launch {
            val process = ProcessBuilder(command)
                .redirectErrorStream(false)
                .start()

            try {
                val stdoutJob = launch {
                    process.inputStream.bufferedReader().useLines { lines ->
                        for (line in lines) {
                            if (!isActive) break
                            onStdout(line)
                        }
                    }
                }

                val stderrJob = launch {
                    process.errorStream.bufferedReader().useLines { lines ->
                        for (line in lines) {
                            if (!isActive) break
                            onStderr(line)
                        }
                    }
                }

                // 使用 withContext + isActive 检查支持取消
                withContext(Dispatchers.IO) {
                    while (isActive && process.isAlive) {
                        delay(100)
                    }
                }

            } finally {
                if (process.isAlive) {
                    process.destroy()
                    if (!process.waitFor(1, TimeUnit.SECONDS)) {
                        process.destroyForcibly()
                        process.waitFor(100L, TimeUnit.MILLISECONDS)
                    }
                    onStderr("[Process killed]")
                }
            }
        }
    }

    fun execute(
        command: String,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
        onStdout: (String) -> Unit = {},
        onStderr: (String) -> Unit = {}
    ): Job {
        val parts = command.split("\\s+".toRegex())
        return executeAsync(
            command = parts,
            coroutineScope = coroutineScope,
            onStdout = onStdout,
            onStderr = onStderr,
        )
    }
}


//fun main() = runBlocking {
//    val job = ShellExecutor.execute("ping -c 1000 www.google.com",
//        onStdout = { println("[OUT] $it") },
//        onStderr = { println("[ERR] $it") }
//    )
//
//    delay(5000) // 运行5秒后取消
//    job.cancel()
////    job.cancelAndJoin()
//    println("命令执行已取消")
//    delay(500000)
//}
