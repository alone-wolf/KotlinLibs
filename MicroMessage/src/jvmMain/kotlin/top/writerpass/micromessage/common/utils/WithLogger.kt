package top.writerpass.micromessage.common.utils

import org.slf4j.Logger

interface WithLogger {
    val logger: Logger

    fun String.logi() {
        logger.info(this)
    }

    fun String.logd() {
        logger.debug(this)
    }

    fun String.logw() {
        logger.warn(this)
    }

    fun String.loge(error: Throwable? = null) {
        logger.error(this, error)
    }

}

inline fun <T> WithLogger.logWrapper(action: String, block: () -> T): T {
    logger.info(">>>>> Start $action")
    val a = block()
    logger.info("<<<<< End $action")
    return a
}