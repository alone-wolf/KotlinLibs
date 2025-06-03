package top.writerpass.kotlinlibrary.file.operator.bytes

import kotlinx.coroutines.CoroutineScope
import top.writerpass.kotlinlibrary.coroutine.launchIO
import top.writerpass.kotlinlibrary.coroutine.withContextIO
import java.io.File

class LegacyConcurrentFileReaderV4(
    private val file: File,
    private val fragmentSize: Int,
) {
    private val fileSize: Long = file.length()
    private val fragmentFullNum: Long = fileSize / fragmentSize
    private val fragmentLastSize: Long = fileSize % fragmentSize
    private val hasLastFragment: Boolean = fragmentLastSize != 0L

    fun printInfo() {
        println("file:${file.name}")
        println("size:${fileSize}")
        println("fragmentSize:${fragmentSize}")
        println("fragmentFullNum:${fragmentFullNum}")
        println("fragmentLastSize:${fragmentLastSize}")
        println("hasLastFragment:${hasLastFragment}")
        println()
    }

    suspend fun readFragment(size: Int, offset: Long): ByteArray {
        return withContextIO {
            val bytes = ByteArray(size)
            file.inputStream().use { input ->
                input.skip(offset)
                input.read(bytes, 0, size)
                bytes
            }
        }
    }

    fun readAll(
        coroutineScope: CoroutineScope,
        onRead: suspend (Int, ByteArray) -> Unit
    ) {
        repeat(fragmentFullNum.toInt()) {
            coroutineScope.launchIO {
                val size = fragmentSize
                val offset = (it * fragmentSize).toLong()
                val bytes = readFragment(size, offset)
                onRead(it, bytes)
            }
        }
        if (hasLastFragment) {
            coroutineScope.launchIO {
                val size = fragmentLastSize.toInt()
                val offset = (fileSize - fragmentLastSize).toLong()
                val bytes = readFragment(size, offset)
                onRead((fragmentFullNum).toInt(), bytes)
            }
        }
    }
}