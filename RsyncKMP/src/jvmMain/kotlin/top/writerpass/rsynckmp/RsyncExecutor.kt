package top.writerpass.rsynckmp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Duration

class RsyncExecutor(private val command: RsyncCommand) {

    fun executeSync(
        inheritIO: Boolean = false,
        timeout: Duration? = null,
        onOutput: ((String) -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ): Int {
        val pb = ProcessBuilder(command.getPlatformCommand())
        if (inheritIO) pb.inheritIO()
        val process = pb.start()

        val stdoutJob = Thread {
            process.inputStream.bufferedReader()
                .useLines { it.forEach { line -> onOutput?.invoke(line) } }
        }.apply { start() }

        val stderrJob = Thread {
            process.errorStream.bufferedReader()
                .useLines { it.forEach { line -> onError?.invoke(line) } }
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

        stdoutJob.join(); stderrJob.join()
        return exitCode
    }

    suspend fun executeAsync(
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
        onOutput: ((String) -> Unit)? = null,
        onError: ((String) -> Unit)? = null,
        onExit: ((Int) -> Unit)? = null,
        timeout: Duration? = null
    ): Job = scope.launch {
        val process = ProcessBuilder(command.getPlatformCommand()).start()

        val stdoutJob = async {
            process.inputStream.bufferedReader()
                .useLines { it.forEach { line -> onOutput?.invoke(line) } }
        }
        val stderrJob = async {
            process.errorStream.bufferedReader()
                .useLines { it.forEach { line -> onError?.invoke(line) } }
        }

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

        stdoutJob.await(); stderrJob.await()
        onExit?.invoke(exitCode)
    }
}

fun RsyncCommand.executor() = RsyncExecutor(this)
