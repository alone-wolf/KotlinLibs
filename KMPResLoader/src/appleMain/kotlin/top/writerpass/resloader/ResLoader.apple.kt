package top.writerpass.resloader

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.stringWithContentsOfFile
import platform.posix.memcpy

actual fun ResourceLoader.isExists(path: String): Boolean {
    val fileName = path.substringBeforeLast(".")
    val fileExt = path.substringAfterLast(".", "")
    return NSBundle.mainBundle.pathForResource(fileName, fileExt) != null
}

@OptIn(ExperimentalForeignApi::class)
actual fun ResourceLoader.readText(path: String): String {
    val file = NSBundle.mainBundle.pathForResource(path, null)
        ?: error("Resource not found: $path")
    return NSString.stringWithContentsOfFile(
        path = file,
        encoding = NSUTF8StringEncoding,
        error = null
    ) as String
}

@OptIn(ExperimentalForeignApi::class)
actual fun ResourceLoader.read(path: String): ByteArray {
    // 这里假设 path 里带扩展名，例如 "config.json"
    val fileName = path.substringBeforeLast(".")
    val fileExt = path.substringAfterLast(".", "")

    val filePath = NSBundle.mainBundle.pathForResource(fileName, fileExt)
        ?: error("Resource not found: $path")

    val data = NSData.dataWithContentsOfFile(filePath)
        ?: error("Unable to read resource: $path")

    return ByteArray(data.length.toInt()).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
    }
}