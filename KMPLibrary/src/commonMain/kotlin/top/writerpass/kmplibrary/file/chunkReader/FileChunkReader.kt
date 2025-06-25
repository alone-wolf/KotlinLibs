package top.writerpass.kmplibrary.file.chunkReader
//
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.cancelAndJoin
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.asFlow
//import kotlinx.coroutines.flow.flatMapMerge
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.takeWhile
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//
//class FileChunkReader(
//    private val file: File,
//    private val chunkSize: Int = 1024 * 1024 * 1,
//    private val concurrency: Int = 4
//) {
//
//    interface Callback {
//        fun onFileStart(totalChunks: Int)
//        fun onChunkStart(index: Int, offset: Long, size: Int)
//        fun onChunkFinished(index: Int, offset: Long, size: Int, bytes: ByteArray)
//        fun onChunkError(index: Int, offset: Long, size: Int, throwable: Throwable)
//        fun onFileFinished()
//        fun onFileError(throwable: Throwable)
//
//        //        fun onProgress(completedChunks: Int, totalChunks: Int)
//        fun shouldContinue(): Boolean // 业务层中止判断
//    }
//
//    private fun calculateChunkBounds(index: Int, totalSize: Long): Pair<Long, Int> {
//        val offset = index * chunkSize.toLong()
//        val size = minOf(chunkSize, (totalSize - offset).toInt())
//        return offset to size
//    }
//
//    private inline fun safeCallback(block: () -> Unit) {
//        try {
//            block()
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//    }
//
//    // 现在read返回Job，方便调用者取消
//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun read(callback: Callback, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): Job {
//        return scope.launch {
//            require(chunkSize > 0) { "chunkSize must be > 0" }
//            require(concurrency > 0) { "concurrency must be > 0" }
//
//            val totalSize = file.length()
//            val totalChunks = ((totalSize + chunkSize - 1) / chunkSize).toInt()
////            val completedChunks = AtomicInteger(0)
//
//            safeCallback { callback.onFileStart(totalChunks) }
//
//            try {
//                (0 until totalChunks).asFlow()
//                    .takeWhile { callback.shouldContinue() && isActive } // 支持业务判断+协程取消
//                    .flatMapMerge(concurrency) { index ->
//                        flow {
//                            val (offset, size) = calculateChunkBounds(index, totalSize)
//                            safeCallback { callback.onChunkStart(index, offset, size) }
//                            try {
//                                val buffer = ByteArray(size)
//                                RandomAccessFile(file, "r").use { raf ->
//                                    raf.seek(offset)
//                                    raf.readFully(buffer)
//                                }
//                                emit(Result.success(ChunkResult(index, offset, size, buffer)))
//                            } catch (e: Throwable) {
//                                emit(Result.failure(e))
//                            }
//                        }.map { result -> Pair(index, result) }
//                    }
//                    .collect { (index, result) ->
//                        val (offset, size) = calculateChunkBounds(index, totalSize)
//
//                        result.onSuccess { chunk ->
//                            safeCallback {
//                                callback.onChunkFinished(
//                                    chunk.index,
//                                    chunk.offset,
//                                    chunk.size,
//                                    chunk.bytes
//                                )
////                                val finished = completedChunks.incrementAndGet()
////                                callback.onProgress(finished, totalChunks)
//                            }
//                        }.onFailure { error ->
//                            safeCallback {
//                                callback.onChunkError(index, offset, size, error)
//                            }
//                        }
//                    }
//
//                safeCallback { callback.onFileFinished() }
//            } catch (e: CancellationException) {
//                // 任务被取消，不视为错误
//                println("FileChunkReader read cancelled")
//            } catch (e: Throwable) {
//                safeCallback { callback.onFileError(e) }
//            }
//        }
//    }
//
//    private data class ChunkResult(
//        val index: Int,
//        val offset: Long,
//        val size: Int,
//        val bytes: ByteArray
//    ) {
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//
//            other as ChunkResult
//
//            if (index != other.index) return false
//            if (offset != other.offset) return false
//            if (size != other.size) return false
//            if (!bytes.contentEquals(other.bytes)) return false
//
//            return true
//        }
//
//        override fun hashCode(): Int {
//            var result = index
//            result = 31 * result + offset.hashCode()
//            result = 31 * result + size
//            result = 31 * result + bytes.contentHashCode()
//            return result
//        }
//    }
//}
//
//suspend fun main(): Unit = coroutineScope {
//    val file = File("D:\\System Images\\Windows 11\\Windows11_26100.2033_Professional_zh-cn_arm64.iso")
//
//    val reader = FileChunkReader(
//        file = file,
//        chunkSize = 1024 * 1024 * 10, // 1MB
//        concurrency = 10
//    )
//
//    val job = reader.read(object : FileChunkReader.Callback {
//        override fun onFileStart(totalChunks: Int) {
//            println("读取开始，共 $totalChunks 个分片")
//        }
//
//        override fun onChunkStart(index: Int, offset: Long, size: Int) {
//            println("开始读取分片 $index at offset=$offset, size=$size")
//        }
//
//        override fun onChunkFinished(index: Int, offset: Long, size: Int, bytes: ByteArray) {
//            println("完成分片 $index (offset=$offset, size=$size)")
//        }
//
//        override fun onChunkError(index: Int, offset: Long, size: Int, throwable: Throwable) {
//            println("分片 $index 读取失败: ${throwable.message}")
//        }
//
//        override fun onFileFinished() {
//            println("文件读取完成")
//        }
//
//        override fun onFileError(throwable: Throwable) {
//            println("文件读取异常: ${throwable.message}")
//        }
//
////        override fun onProgress(completedChunks: Int, totalChunks: Int) {
////            println("进度: $completedChunks / $totalChunks")
////        }
//
//        override fun shouldContinue(): Boolean {
//            return true
//        }
//    })
//
//
//    delay(5000)
//    println("取消读取任务")
//    job.cancelAndJoin()
//}