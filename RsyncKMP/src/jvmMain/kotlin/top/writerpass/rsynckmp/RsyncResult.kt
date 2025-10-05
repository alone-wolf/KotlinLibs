package top.writerpass.rsynckmp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.Duration

data class RsyncResult(
    val exitCode: Int,
    val stdout: List<String>,
    val stderr: List<String>
)

fun RsyncCommand.executeWithResult(timeout: Duration? = null): RsyncResult {
    val stdout = mutableListOf<String>()
    val stderr = mutableListOf<String>()
    val pb = ProcessBuilder(getPlatformCommand())
    val process = pb.start()

    val outThread = Thread {
        process.inputStream.bufferedReader().useLines { it.forEach { line -> stdout.add(line) } }
    }.apply { start() }
    val errThread = Thread {
        process.errorStream.bufferedReader().useLines { it.forEach { line -> stderr.add(line) } }
    }.apply { start() }

    val exitCode = if (timeout != null) {
        if (process.waitFor(
                timeout.toMillis(),
                java.util.concurrent.TimeUnit.MILLISECONDS
            )
        ) process.exitValue()
        else {
            process.destroyForcibly(); -1
        }
    } else process.waitFor()

    outThread.join(); errThread.join()
    return RsyncResult(exitCode, stdout, stderr)
}

suspend fun RsyncCommand.executeWithResultAsync(timeout: Duration? = null): RsyncResult =
    CoroutineScope(Dispatchers.IO).async { executeWithResult(timeout) }.await()

enum class RsyncDryRunType { ADD, UPDATE, DELETE }
data class RsyncDryRunAction(val path: String, val type: RsyncDryRunType)

fun RsyncResult.parseDryRun(): List<RsyncDryRunAction> = stdout.mapNotNull { line ->
    when {
        line.startsWith("*deleting ") -> RsyncDryRunAction(
            line.removePrefix("*deleting ").trim(),
            RsyncDryRunType.DELETE
        )

        line.isNotBlank() -> RsyncDryRunAction(line.trim(), RsyncDryRunType.ADD) // 可根据需求扩展 UPDATE
        else -> null
    }
}
