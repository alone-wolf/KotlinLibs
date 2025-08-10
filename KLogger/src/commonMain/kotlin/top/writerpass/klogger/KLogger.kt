@file:OptIn(ExperimentalTime::class)

package top.writerpass.klogger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Map.copyOf
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.time.ExperimentalTime

class KLogger private constructor() {

    val logList = ConcurrentLinkedDeque<Log>()
    private val _logFlow = MutableSharedFlow<Log>()
    val logFlow = _logFlow.asSharedFlow()

    private suspend fun logSaver(log: Log) {
        logList.add(log)
        _logFlow.emit(log)
    }

    suspend fun baseLog(
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

    suspend fun error(
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

    suspend fun debug(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.DEBUG,
        tag = tag,
        message = message,
        source = source
    )

    suspend fun info(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.INFO,
        tag = tag,
        message = message,
        source = source
    )

    suspend fun warn(
        tag: String,
        message: String,
        source: LogSources = LogSources.NORMAL
    ) = baseLog(
        level = LogLevel.WARN,
        tag = tag,
        message = message,
        source = source
    )

    suspend fun verbose(
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
        private val _loggers = ConcurrentHashMap<String, KLogger>()

        val default: KLogger = _loggers.getOrPut("default") { KLogger() }

        fun instance(name: String): KLogger = _loggers.getOrPut(name) { KLogger() }
    }
}

