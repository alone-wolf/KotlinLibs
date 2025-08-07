@file:OptIn(ExperimentalTime::class)

package top.writerpass.klogger

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class Log(
    val tag: String,
    val message: String,
    val error: Throwable? = null,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val level: LogLevel,
    val source: LogSources = LogSources.NORMAL
)