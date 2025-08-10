@file:OptIn(ExperimentalTime::class)

package top.writerpass.kmplibrary.datetime

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


object DateTimeUtils {

    /**
     * 获取当前 UTC 时间戳（毫秒）
     */
    fun currentUtcMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    /**
     * 获取当前系统时区
     */
    fun currentTimeZone(): TimeZone {
        return TimeZone.currentSystemDefault()
    }

    /**
     * 将 Unix 毫秒时间戳格式化为 yyyy/MM/dd HH:mm:ss.SSS
     * @param timeZone 要格式化成的时区（默认本地时区）
     */
    fun Long.formatUnixMs(timeZone: TimeZone = currentTimeZone()): String {
        val ldt = Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(timeZone)

        fun Int.pad(digits: Int) = toString().padStart(digits, '0')

        return "${ldt.year.pad(4)}/${ldt.month.number.pad(2)}/${ldt.day.pad(2)} " +
                "${ldt.hour.pad(2)}:${ldt.minute.pad(2)}:${ldt.second.pad(2)}." +
                (ldt.nanosecond / 1_000_000).pad(3)
    }
}
