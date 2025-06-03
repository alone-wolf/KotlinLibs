package top.writerpass.kotlinlibrary.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")
private val zoneId = ZoneId.systemDefault()

fun Long.formatyyyyMMddHHmmssSSS(): String {
    val instant = Instant.ofEpochMilli(this)
    val zonedDateTime = instant.atZone(zoneId)
    val formattedTime = zonedDateTime.format(formatter)
    return formattedTime
}