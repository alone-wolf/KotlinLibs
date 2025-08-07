package top.writerpass.klogger

class KLoggerWrapper(
    val instance: KLogger = KLogger.default,
    val tag: String,
    val source: LogSources = LogSources.NORMAL
) {
    fun error(message: String, error: Throwable? = null) = instance.error(
        tag = tag,
        message = message,
        error = error,
        source = source
    )

    fun debug(message: String) = instance.debug(
        tag = tag,
        message = message,
        source = source
    )

    fun info(message: String) = instance.info(
        tag = tag,
        message = message,
        source = source
    )

    fun warn(message: String) = instance.warn(
        tag = tag,
        message = message,
        source = source
    )

    fun verbose(message: String) = instance.verbose(
        tag = tag,
        message = message,
        source = source
    )
}

fun KLoggerWrapper.e(message: String, error: Throwable? = null) =
    instance.error(
        tag = tag,
        message = message,
        error = error,
        source = source
    )

fun KLoggerWrapper.d(message: String) = instance.debug(
    tag = tag,
    message = message,
    source = source
)

fun KLoggerWrapper.i(message: String) = instance.info(
    tag = tag,
    message = message,
    source = source
)

fun KLoggerWrapper.w(message: String) = instance.warn(
    tag = tag,
    message = message,
    source = source
)

fun KLoggerWrapper.v(message: String) = instance.verbose(
    tag = tag,
    message = message,
    source = source
)