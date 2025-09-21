package top.writerpass.resloader

// src/commonMain/kotlin/ResourceLoader.kt
object ResourceLoader

class KRes(val path: String) {
    fun isExists(): Boolean {
        return ResourceLoader.isExists(path)
    }

    fun read(): ByteArray {
        return ResourceLoader.read(path)
    }

    fun readText(): String {
        return ResourceLoader.readText(path)
    }
}

expect fun ResourceLoader.isExists(path: String): Boolean
expect fun ResourceLoader.read(path: String): ByteArray
expect fun ResourceLoader.readText(path: String): String
