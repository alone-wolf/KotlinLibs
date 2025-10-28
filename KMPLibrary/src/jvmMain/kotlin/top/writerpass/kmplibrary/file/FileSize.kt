package top.writerpass.kmplibrary.file

import java.io.File
import kotlin.math.log10
import kotlin.math.pow

/**
 * 返回文件大小的友好字符串表示，例如：
 *  - 532 -> "532 B"
 *  - 15360 -> "15.0 KB"
 *  - 1048576 -> "1.0 MB"
 *  - 3221225472 -> "3.0 GB"
 */
@Suppress("DefaultLocale")
fun File.friendlySize(): String {
    if (!exists() || !isFile) return "0 B"
    val size = length()
    if (size <= 0) return "0 B"

    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    val displaySize = size / 1024.0.pow(digitGroups.toDouble())

    return String.format("%.1f %s", displaySize, units[digitGroups])
}