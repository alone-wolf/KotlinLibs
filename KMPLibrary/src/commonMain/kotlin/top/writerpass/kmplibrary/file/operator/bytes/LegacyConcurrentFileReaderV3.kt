package top.writerpass.kmplibrary.file.operator.bytes

//import kotlinx.coroutines.CoroutineScope
//import top.writerpass.kotlinlibrary.coroutine.launchIO
//import top.writerpass.kotlinlibrary.coroutine.withContextIO
//import java.io.File
//
//class LegacyConcurrentFileReaderV3(
//    private val file: File,
//    private val fragmentSize: Int,
//) {
//    private val fileSize: Long = file.length()
//    private val fragmentFullNum: Long = fileSize / fragmentSize
//    private val fragmentLastSize: Long = fileSize % fragmentSize
//    private val hasLastFragment: Boolean = fragmentLastSize != 0L
//
//    fun printInfo() {
//        println("file:${file.name}")
//        println("size:${fileSize}")
//        println("fragmentSize:${fragmentSize}")
//        println("fragmentFullNum:${fragmentFullNum}")
//        println("fragmentLastSize:${fragmentLastSize}")
//        println("hasLastFragment:${hasLastFragment}")
//        println()
//    }
//
//    suspend fun readFragment(size: Int, offset: Long): ByteArray {
//        return withContextIO {
//            val bytes = ByteArray(size)
//            file.inputStream().use { input ->
//                input.skip(offset)
//                input.read(bytes, 0, size)
//                bytes
//            }
//
//        }
//    }
//
//    private suspend fun singleRead(
//        index: Int,
//        onRead: suspend (Int, ByteArray) -> Unit
//    ) {
//        val size: Int
//        val offset: Long
//
//        if (index < fragmentFullNum) {
//            size = fragmentSize
//            offset = (index * fragmentSize).toLong()
//        } else {
//            size = fragmentLastSize.toInt()
//            offset = (fileSize - fragmentLastSize).toLong()
//        }
//        val bytes = readFragment(size, offset)
//        onRead(index, bytes)
//    }
//
//    fun readAll(
//        coroutineScope: CoroutineScope,
//        onRead: suspend (Int, ByteArray) -> Unit
//    ) {
//        val totalFragments = fragmentFullNum + if (hasLastFragment) 1 else 0
//        repeat(totalFragments.toInt()) {
//            coroutineScope.launchIO {
//                singleRead(it, onRead)
//            }
//        }
//    }
//}
//
//fun main(): Unit = runBlocking {
//    val file = File("C:\\Users\\wolf\\Documents\\aaaa.txt")
//    val reader = LegacyConcurrentFileReaderV3(file, 10)
//    reader.printInfo()
//    reader.readAll(this) { index, it ->
//        println("$index: ${it.decodeToString()}")
//    }
//}