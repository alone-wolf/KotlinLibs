package top.writerpass.kotlinlibrary.file.operator

import top.writerpass.kotlinlibrary.file.operator.bytes.RandomConcurrentFileReaderV7
import top.writerpass.kotlinlibrary.file.operator.bytes.RandomConcurrentFileWriterV7
import java.io.File

// TODO: 下一步修改为小ByteArray的循环读取从而凑够分片，不过目前的已经足以使用，可以跑

class FileShardTool(
    val file: File,
    val sliceSize: Long
) {
    internal val fileSize: Long = file.length()
    internal val sliceFullNum: Long = fileSize / sliceSize
    internal val sliceLastSize: Long = fileSize % sliceSize
    internal val hasLastSlice: Boolean = sliceLastSize != 0L

    data class FileSliceStatus(
        val fileSize: Long,
        val sliceSize: Long,
        val sliceFullNum: Long,
        val sliceLastSize: Long,
        val hasLastSlice: Boolean
    )

    fun onSliceFullNum(block: (Long) -> Unit): FileShardTool {
        block(sliceFullNum)
        return this
    }

    fun onSliceLastSize(block: (Long) -> Unit): FileShardTool {
        block(sliceLastSize)
        return this
    }

    fun printInfo(block: (FileSliceStatus) -> Unit = {}): FileShardTool {
        println()
        println("file: ${file.name}")
        println("size: $fileSize")
        println("sliceSize: $sliceSize")
        println("sliceFullNum: $sliceFullNum")
        println("sliceLastSize: $sliceLastSize")
        println("hasLastSlice: $hasLastSlice")
        println()
        block(
            FileSliceStatus(
                fileSize = fileSize,
                sliceSize = sliceSize,
                sliceFullNum = sliceFullNum,
                sliceLastSize = sliceLastSize,
                hasLastSlice = hasLastSlice
            )
        )
        return this
    }

    fun reader(concurrencyNum: Int): RandomConcurrentFileReaderV7 {
        return RandomConcurrentFileReaderV7(
            file = file,
            fileSize = file.length(),
            sliceSize = sliceSize,
            sliceFullNum = sliceFullNum,
            sliceLastSize = sliceLastSize,
            hasLastSlice = hasLastSlice,
            concurrencyNum = concurrencyNum
        )
    }

    fun writer(newFile: File): RandomConcurrentFileWriterV7 {
        return RandomConcurrentFileWriterV7(
            fragmentSize = sliceSize,
            fragmentFullNum = sliceFullNum,
            fragmentLastSize = sliceLastSize,
            hasLastFragment = hasLastSlice,
            newFile = newFile
        )
    }
}