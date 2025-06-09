package top.writerpass.kotlinlibrary.file.operator.bytes

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import top.writerpass.kotlinlibrary.coroutine.withContextIO
import java.io.File
import java.io.RandomAccessFile
import java.util.concurrent.atomic.AtomicInteger

class FlowRandomConcurrentFileReaderV8(
    val file: File,
    val sliceSize: Int,
    val threadNum: Int = 3,
    val onSliceRead: suspend (index: Int, bytes: ByteArray) -> Unit
) {
    val sliceNum: Int = ((file.length() + sliceSize - 1) / sliceSize).toInt()

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow: Flow<Boolean> = flow {
        val completed = AtomicInteger(0)
        (0 until sliceNum).asFlow()
            .flatMapMerge(threadNum) { index ->
                flow {
                    val bytes = withContextIO {
                        RandomAccessFile(file, "r").use { raf ->
                            raf.seek((index * sliceSize).toLong())
                            ByteArray(sliceSize).apply {
                                val readSize = raf.read(this)
                                if (readSize < 0) byteArrayOf() else copyOf(readSize)
                            }
                        }
                    }
                    if (bytes.isNotEmpty()) {
                        onSliceRead(index, bytes)
                        emit(true)
                    } else {
                        emit(false)
                    }
                }
            }.collect { r -> if (r) completed.incrementAndGet() }
        if (completed.get() == sliceNum) {

            emit(true)
        }else{
            emit(false)
        }
    }
}