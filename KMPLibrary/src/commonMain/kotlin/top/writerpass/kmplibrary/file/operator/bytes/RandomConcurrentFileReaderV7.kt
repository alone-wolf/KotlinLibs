//package top.writerpass.kmplibrary.file.operator.bytes
//
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.sync.Semaphore
//import top.writerpass.kotlinlibrary.coroutine.launchIO
//import top.writerpass.kotlinlibrary.coroutine.withContextIO
//import java.io.File
//import java.io.RandomAccessFile
//
//class RandomConcurrentFileReaderV7 internal constructor(
//    private val file: File,
//    private val fileSize: Long = file.length(),
//    private val sliceSize: Long,
//    private val sliceFullNum: Long,
//    private val sliceLastSize: Long,
//    private val hasLastSlice: Boolean = sliceLastSize > 0,
//    private val concurrencyNum: Int
//) {
////    suspend fun createBlankFile(newFile: File) {
////        val array = ByteArray(fragmentSize.toInt())
////        withContextIO {
////            newFile.outputStream().use { output ->
////                repeat(fragmentFullNum.toInt()) {
////                    output.write(array)
////                }
////                if (hasLastFragment) {
////                    output.write(ByteArray(fragmentLastSize.toInt()))
////                }
////            }
////        }
////    }
////
////    suspend fun writeFragment(offset: Long, bytes: ByteArray) {
////        withContextIO {
////            RandomAccessFile(newFile, "rw").use { raf ->
////                raf.seek(offset)
////                raf.write(bytes)
////            }
////        }
////    }
//
//    suspend fun readFragment(size: Long, offset: Long): ByteArray {
//        return withContextIO {
//            val bytes = ByteArray(size.toInt())
//            RandomAccessFile(file, "r").use { raf ->
//                raf.seek(offset)
//                raf.readFully(bytes)
//            }
//            bytes
//        }
//    }
//
//    fun readAll(
//        coroutineScope: CoroutineScope,
//        onRead: suspend (Int, Long, ByteArray) -> Unit,
//    ) {
//        val total = sliceFullNum.toInt() + if (hasLastSlice) 1 else 0
//        val actualConcurrency = minOf(concurrencyNum, total)
//        val semaphore = Semaphore(actualConcurrency)
//
//        repeat(sliceFullNum.toInt()) {
//            coroutineScope.launchIO {
//                semaphore.acquire()
//                val size = sliceSize
//                val offset = (it * sliceSize).toLong()
//                val bytes = readFragment(size, offset)
//                onRead(it, offset, bytes)
//                semaphore.release()
//            }
//        }
//        if (hasLastSlice) {
//            coroutineScope.launchIO {
//                semaphore.acquire()
//                val size = sliceLastSize
//                val offset = (fileSize - sliceLastSize).toLong()
//                val bytes = readFragment(size, offset)
//                onRead((sliceFullNum).toInt(), offset, bytes)
//                semaphore.release()
//            }
//        }
//    }
//}