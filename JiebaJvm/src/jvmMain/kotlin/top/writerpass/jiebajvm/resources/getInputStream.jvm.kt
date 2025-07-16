package top.writerpass.jiebajvm.resources

import java.io.InputStream

actual fun getInputStream(path: String): InputStream {
    val stream = object {}.javaClass.classLoader
        .getResourceAsStream(path)
        ?: error("Resource not found: $path")
    return stream
}