//package top.writerpass.kmplibrary.file.operator.bytes
//
//import java.io.File
//
//class LegacyFileReaderV0(
//    private val file: File,
//    private val fragmentSize: Int
//) : Iterable<ByteArray> {
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
//    fun readFullFragment(index: Int): ByteArray {
//        val bytes = ByteArray(fragmentSize)
//        val skipOffset = (index * fragmentSize).toLong()
//        return file.inputStream().use { input ->
//            input.skip(skipOffset)
//            input.read(bytes, 0, fragmentSize)
//            bytes
//        }
//    }
//
//    fun readLastFragment(): ByteArray {
//        val bytes = ByteArray(fragmentLastSize.toInt())
//        val skipOffset = fileSize - fragmentLastSize
//        return file.inputStream().use { input ->
//            input.skip(skipOffset)
//            input.read(bytes, 0, fragmentLastSize.toInt())
//            bytes
//        }
//    }
//
//    override fun iterator(): Iterator<ByteArray> {
//        return object : Iterator<ByteArray> {
//            var index = 0
//            override fun hasNext(): Boolean {
//                val hasNext = if (hasLastFragment) {
//                    index < fragmentFullNum + 1
//                } else {
//                    index < fragmentFullNum
//                }
//                return hasNext
//            }
//
//            override fun next(): ByteArray {
//                val array = if (index < fragmentFullNum) {
//                    readFullFragment(index)
//                } else {
//                    readLastFragment()
//                }
//                index += 1
//                return array
//            }
//        }
//    }
//}
//
////fun main() {
////    val file = File("C:\\Users\\wolf\\Documents\\aaaa.txt")
////    val reader = LegacyFileReaderV0(file, 10)
////    reader.forEachIndexed { index, it ->
////        println(index.toString() + ": " + it.decodeToString())
////    }
////}