package top.writerpass.resloader

import java.io.InputStream


private fun getAsStream(path: String): InputStream {
    val stream = object {}.javaClass.classLoader?.getResourceAsStream(path)
        ?: error("Resource not found: $path")
    return stream
}

actual fun ResourceLoader.isExists(path: String): Boolean {
    return object {}.javaClass.classLoader?.getResource(path) != null
}

actual fun ResourceLoader.readText(path: String): String {
    val stream = getAsStream(path)
    return stream.bufferedReader().use { it.readText() }
}

actual fun ResourceLoader.read(path: String): ByteArray {
    val stream = getAsStream(path)
    return stream.use { it.readBytes() }
}
