package top.writerpass.klogger

class KLoggerWrapper(
    val instance: KLogger = KLogger.default,
    val tag: String,
    val source: LogSources = LogSources.NORMAL
) {
    // error/debug/info/warn/verbose
    fun error(message: String, error: Throwable? = null) = instance.error(tag, message, error, source)
    fun debug(message: String) = instance.debug(tag, message, source)
    fun info(message: String) = instance.info(tag, message, source)
    fun warn(message: String) = instance.warn(tag, message, source)
    fun verbose(message: String) = instance.verbose(tag, message, source)
}