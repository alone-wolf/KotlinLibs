package top.writerpass.rsynckmp

import java.util.concurrent.TimeUnit


fun RsyncCommand.getPlatformCommand(): List<String> {
    val cmd = toCommandString()
    return if (isWindows()) {
        if (isCygwinAvailable() || isWslAvailable()) listOf("bash", "-c") + cmd
        else listOf("cmd", "/c") + cmd
    } else listOf("/bin/sh", "-c") + cmd
}

fun isCygwinAvailable(): Boolean = System.getenv("CYGWIN")?.isNotBlank() == true
fun isWslAvailable(): Boolean = try {
    val p = ProcessBuilder("wsl", "--version").start()
    p.waitFor(1, TimeUnit.SECONDS)
} catch (e: Exception) {
    false
}

fun isWindows() = System.getProperty("os.name").lowercase().contains("win")
