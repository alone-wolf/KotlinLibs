package top.writerpass.kotlinlibrary.file.operator.bytes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import top.writerpass.kotlinlibrary.coroutine.launchIO
import top.writerpass.kotlinlibrary.coroutine.withContextIO
import java.io.File
import java.io.RandomAccessFile

class RandomConcurrentFileReaderV6(
    private val file: File,
    private val fragmentSize: Int,
    private val concurrencyLevel: Int
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
            RandomAccessFile(file, "r").use { raf ->
                raf.seek(offset)
                raf.readFully(bytes)
            }
            bytes
        }
    }

    fun readAll(
        coroutineScope: CoroutineScope,
        onRead: suspend (Int, ByteArray) -> Unit
    ) {
        val total = fragmentFullNum.toInt() + if (hasLastFragment) 1 else 0
        val actualConcurrency = minOf(concurrencyLevel, total)
        val channel = Channel<Unit>(actualConcurrency)

        repeat(fragmentFullNum.toInt()) {
            coroutineScope.launchIO {
                channel.send(Unit)
                val size = fragmentSize
                val offset = (it * fragmentSize).toLong()
                val bytes = readFragment(size, offset)
                onRead(it, bytes)
                delay(1000L)
                channel.receive()
            }
        }
        if (hasLastFragment) {
            coroutineScope.launchIO {
                channel.send(Unit)
                val size = fragmentLastSize.toInt()
                val offset = (fileSize - fragmentLastSize).toLong()
                val bytes = readFragment(size, offset)
                onRead((fragmentFullNum).toInt(), bytes)
                delay(1000L)
                channel.receive()
            }
        }
    }
}