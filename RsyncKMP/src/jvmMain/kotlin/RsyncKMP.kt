import kotlinx.coroutines.runBlocking
import top.writerpass.rsynckmp.executeWithResult
import top.writerpass.rsynckmp.executor
import top.writerpass.rsynckmp.parseDryRun
import top.writerpass.rsynckmp.rsync

fun main() = runBlocking {
    val cmd = rsync {
        source("wolf@192.168.50.10:/mnt/primary/images/Windows\\ 10/cn_windows_10_enterprise_ltsc_2019_x64_dvd_9c09ff24.iso")
        destination("~/Downloads/aa")
        archive = true
        compress = true
        progress = true
        delete = true
        dryRun = true
//        ssh { port = 22; privateKey = "~/.ssh/id_rsa" }
    }

    val result = cmd.executeWithResult()
    println("Exit: ${result.exitCode}")
    result.parseDryRun().forEach { println("${it.type}: ${it.path}") }

    // 异步执行示例
    cmd.executor().executeAsync(
        onOutput = { println("[OUT] $it") },
        onError = { println("[ERR] $it") },
        onExit = { println("[EXIT] $it") }
    ).join()
}
