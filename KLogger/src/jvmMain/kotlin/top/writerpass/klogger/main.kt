package top.writerpass.klogger

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        KLogger.default.logFlow.collect { println(it) }
    }
    while (true) {
        delay(700)
        KLogger.wrap("WH_") {
            debug("debug")
            info("info")
            warn("warn")
            error("error")
        }
    }
}