@file:OptIn(ExperimentalTime::class)

package top.writerpass.klogger

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.time.ExperimentalTime

class KLogger private constructor() {

    val logList = ConcurrentLinkedDeque<Log>()
    private val _logFlow = MutableSharedFlow<Log>()
    val logFlow = _logFlow.asSharedFlow()

    private fun logSaver(log: Log) {
        logList.add(log)
        _logFlow.tryEmit(log)
    }

    fun baseLog(
        level: LogLevel,
        tag: String,
        message: String,
        error: Throwable? = null,
        source: LogSources = LogSources.NORMAL
    ) {
        val log = Log(
            tag = tag,
            message = message,
            level = level,
            error = error,
            source = source,
        )
        logSaver(log)
    }

    fun error(
        tag: String,
        message: String,
        error: Throwable? = null,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.ERROR,
        tag = tag,
        message = message,
        error = error,
        source = source
    )

    fun debug(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.DEBUG,
        tag = tag,
        message = message,
        source = source
    )

    fun info(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.INFO,
        tag = tag,
        message = message,
        source = source
    )

    fun warn(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.WARN,
        tag = tag,
        message = message,
        source = source
    )

    fun verbose(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.VERBOSE,
        tag = tag,
        message = message,
        source = source
    )

    companion object {
        private val loggers = ConcurrentHashMap<String, KLogger>()
        val default: KLogger by lazy { loggers.getOrPut("default") { KLogger() } }

        fun instance(name: String): KLogger = loggers.getOrPut(name) { KLogger() }

        fun wrap(
            tag: String,
            source: LogSources = LogSources.NORMAL,
            name: String = "default",
            block: KLoggerWrapper.() -> Unit
        ) {
            val instance = instance(name)
            val w = KLoggerWrapper(instance, tag, source)
            w.block()
        }
    }
}

//suspend operator fun KLogger.Companion.invoke(
//    tag: String,
//    instance: KLogger = KLogger.default,
//    block: suspend KLogger.(String) -> Unit
//) {
//    instance.block(tag)
//}

