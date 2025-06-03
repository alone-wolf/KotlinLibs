package top.writerpass.kotlinlibrary.file.operator.bytes

import java.io.File
import java.io.FileInputStream

class LegacyFileReaderV2(
    private val file: File,
    private val fragmentSize: Int,
    private val inputStream: FileInputStream = file.inputStream()
) : Iterable<ByteArray> {
    private val fileSize: Long = file.length()
    private val fragmentFullNum: Long = fileSize / fragmentSize
    private val fragmentLastSize: Long = fileSize % fragmentSize
    private val hasLastFragment: Boolean = fragmentLastSize != 0L
    private val bytes: ByteArray = ByteArray(fragmentSize)

    fun printInfo() {
        println("file:${file.name}")
        println("size:${fileSize}")
        println("fragmentSize:${fragmentSize}")
        println("fragmentFullNum:${fragmentFullNum}")
        println("fragmentLastSize:${fragmentLastSize}")
        println("hasLastFragment:${hasLastFragment}")
        println()
    }

    fun readFullFragment(): ByteArray {
        inputStream.read(bytes, 0, fragmentSize)
        return bytes
    }

    fun readLastFragment(): ByteArray {
        val bytes = ByteArray(fragmentLastSize.toInt())
        inputStream.read(bytes, 0, fragmentLastSize.toInt())
        return bytes
    }

    override fun iterator(): Iterator<ByteArray> {
        return object : Iterator<ByteArray> {
            var index = 0
            override fun hasNext(): Boolean {
                val hasNext = if (hasLastFragment) {
                    index < fragmentFullNum + 1
                } else {
                    index < fragmentFullNum
                }
                return hasNext
            }

            override fun next(): ByteArray {
                val array = if (index < fragmentFullNum) {
                    readFullFragment()
                } else {
                    readLastFragment()
                }
                index += 1
                return array
            }
        }
    }
}

//fun main() {
//    val file = File("C:\\Users\\wolf\\Documents\\aaaa.txt")
//    val reader = LegacyFileReaderV2(file, 10)
//    reader.forEachIndexed { index, it ->
//        println(index.toString() + ": " + it.decodeToString())
//    }
//}