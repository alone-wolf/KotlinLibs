package top.writerpass.kotlinlibrary.shell

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import top.writerpass.kotlinlibrary.utils.println

suspend fun shellCommand(scope: CoroutineScope, cmd: () -> Array<String>) {
    val runtime = Runtime.getRuntime()
    val p = runtime.exec(cmd())
    val stdout = scope.async {
        p.inputStream.use {
            it.readAllBytes().decodeToString()
        }
    }
    val stderr = scope.async {
        p.errorStream.use {
            it.readAllBytes().decodeToString()
        }
    }
    val code = p.waitFor()
    """
        Command: ${cmd().joinToString(" ")}
        Exit code: $code
        Stdout: ${stdout.await()}
        Stderr: ${stderr.await()}
    """.trimIndent().println()
}