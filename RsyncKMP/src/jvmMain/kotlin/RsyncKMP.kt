import kotlinx.coroutines.runBlocking
import top.writerpass.rsynckmp.executeWithResult
import top.writerpass.rsynckmp.executor
import top.writerpass.rsynckmp.parseDryRun
import top.writerpass.rsynckmp.rsync

fun main() = runBlocking {
    val cmd = rsync {
        source("~/Downloads/blobsaver-arm-3.6.0.dmg")
        destination("~/Downloads/blobsaver-arm-3.6.0.dmg1111")
        archive = true
        compress = true
        progress = true
        delete = true
        dryRun = true
//        ssh { port = 22; privateKey = "~/.ssh/id_rsa" }
    }

    val result = cmd.executeWithResult()
    result.parseDryRun().forEach { println("${it.type}: ${it.path}") }

    // 异步执行示例
    cmd.executor().executeAsync(
        onOutput = { println("[OUT] $it") },
        onError = { println("[ERR] $it") },
        onExit = { println("[EXIT] $it") }
    ).join()
}
