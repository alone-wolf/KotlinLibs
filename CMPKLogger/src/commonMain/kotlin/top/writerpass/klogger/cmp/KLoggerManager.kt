package top.writerpass.klogger.cmp

import androidx.compose.runtime.mutableStateMapOf
import top.writerpass.klogger.KLogger

object KLoggerManager {
    val loggersMap = mutableStateMapOf<String, KLogger>()

    val default: KLogger
        get() {
            return instance("default")
        }

    fun instance(name:String): KLogger {
        return if (loggersMap.containsKey(name)){
            loggersMap[name]!!
        }else{
            val logger = KLogger.instance(name)
            loggersMap[name] = logger
            logger
        }
    }
}