package top.writerpass.kmplibrary.shell

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

object ShellExecutor {

    fun execBlockly(command: String): ShellResult {
        val parts = command.split("\\s+".toRegex()).toTypedArray()
        return execBlockly(parts)
    }

    fun execBlockly(command: Array<String>): ShellResult {
        val process = ProcessBuilder(*command)
            .redirectErrorStream(false)
            .start()
        val stdout = process.inputStream.bufferedReader().readText()
        val stderr = process.errorStream.bufferedReader().readText()
        val exitCode = process.waitFor()
        return ShellResult(
            exitCode = exitCode,
            stdout = stdout,
            stderr = stderr
        )
    }

    suspend fun exec(command: String): ShellResult {
        return exec(command.split(" ").toTypedArray())
    }

    suspend fun exec(command: Array<String>): ShellResult = withContext(Dispatchers.IO) {
        val process = ProcessBuilder(*command)
            .redirectErrorStream(false)
            .start()

        val stdout = StringBuilder()
        val stderr = StringBuilder()

        val stdoutReader = BufferedReader(InputStreamReader(process.inputStream))
        val stderrReader = BufferedReader(InputStreamReader(process.errorStream))

        val stdoutJob = thread {
            stdoutReader.useLines { lines ->
                lines.forEach { stdout.appendLine(it) }
            }
        }

        val stderrJob = thread {
            stderrReader.useLines { lines ->
                lines.forEach { stderr.appendLine(it) }
            }
        }

        val exitCode = process.waitFor()
        stdoutJob.join()
        stderrJob.join()

        return@withContext ShellResult(
            exitCode,
            stdout.toString().trim(),
            stderr.toString().trim()
        )
    }


    fun execute(
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
        onStdout: (String) -> Unit = {},
        onStderr: (String) -> Unit = {}
    ): Job {
        val parts = command.split("\\s+".toRegex())
        return execute(
            command = parts,
            onStdout = onStdout,
            onStderr = onStderr,
        )
    }
}