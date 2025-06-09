//package top.writerpass.kmplibrary.file.operator.bytes
//
//import top.writerpass.kotlinlibrary.coroutine.withContextIO
//import java.io.File
//import java.io.RandomAccessFile
//
//class RandomConcurrentFileWriterV7 internal constructor(
//    private val fragmentSize: Long,
//    private val fragmentFullNum: Long,
//    private val fragmentLastSize: Long,
//    private val hasLastFragment: Boolean,
//    private val newFile: File
//) {
//    suspend fun createBlankFile() {
//        val array = ByteArray(fragmentSize.toInt())
//        withContextIO {
//            newFile.outputStream().use { output ->
//                repeat(fragmentFullNum.toInt()) {
//                    output.write(array)
//                }
//                if (hasLastFragment) {
//                    output.write(ByteArray(fragmentLastSize.toInt()))
//                }
//            }
//        }
//    }
//
//    suspend fun writeFragment(offset: Long, bytes: ByteArray) {
//        withContextIO {
//            RandomAccessFile(newFile, "rw").use { raf ->
//                raf.seek(offset)
//                raf.write(bytes)
//            }
//        }
//    }
//}