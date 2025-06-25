package top.writerpass.ktorm.client

//import io.ktor.client.HttpClient
//import io.ktor.client.request.put
//import io.ktor.client.request.setBody
//import io.ktor.client.statement.HttpResponse
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import io.ktor.http.headers
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.cancelAndJoin
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import top.writerpass.kotlinlibrary.file.chunkReader.FileChunkReader
//import java.io.File
//
//class FileChunkUploaderContentRange(
//    private val file: File,
//    private val uploadUrl: String,
//    private val client: HttpClient,
//    private val chunkSize: Int = 1024 * 1024, // 1MB
//    private val concurrency: Int = 4
//) {
//
//    interface Callback {
//        fun onUploadStart(totalChunks: Int)
//        fun onChunkUploadStart(index: Int, offset: Long, size: Int)
//        fun onChunkUploadSuccess(index: Int, offset: Long, size: Int, response: HttpResponse)
//        fun onChunkUploadError(index: Int, offset: Long, size: Int, throwable: Throwable)
//        fun onUploadFinished()
//        fun onUploadError(throwable: Throwable)
//        fun shouldContinue(): Boolean
//    }
//
//    fun upload(callback: Callback, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): Job {
//        val reader = FileChunkReader(file, chunkSize, concurrency)
//
//        return reader.read(object : FileChunkReader.Callback {
//            override fun onFileStart(totalChunks: Int) {
//                callback.onUploadStart(totalChunks)
//            }
//
//            override fun onChunkStart(index: Int, offset: Long, size: Int) {
//                callback.onChunkUploadStart(index, offset, size)
//            }
//
//            override fun onChunkFinished(index: Int, offset: Long, size: Int, bytes: ByteArray) {
//                scope.launch {
//                    val contentRange = "bytes $offset-${offset + size - 1}/${file.length()}"
//                    try {
//                        val response = client.put(uploadUrl) {
//                            setBody(bytes)
//                            headers {
//                                append(
//                                    HttpHeaders.ContentType,
//                                    ContentType.Application.OctetStream.toString()
//                                )
//                                append(HttpHeaders.ContentRange, contentRange)
//                                append(HttpHeaders.ContentLength, size.toString())
//                                append("File-Name", file.name) // 可选，自定义信息
//                            }
//                        }
//                        callback.onChunkUploadSuccess(index, offset, size, response)
//                    } catch (e: Throwable) {
//                        callback.onChunkUploadError(index, offset, size, e)
//                    }
//                }
//            }
//
//            override fun onChunkError(index: Int, offset: Long, size: Int, throwable: Throwable) {
//                callback.onChunkUploadError(index, offset, size, throwable)
//            }
//
//            override fun onFileFinished() {
//                callback.onUploadFinished()
//            }
//
//            override fun onFileError(throwable: Throwable) {
//                callback.onUploadError(throwable)
//            }
//
//            override fun shouldContinue(): Boolean {
//                return callback.shouldContinue()
//            }
//        }, scope)
//    }
//}
//
//
//suspend fun main() = coroutineScope {
//    val file = File("your-file-path.iso")
//    val client = HttpClient()
//    val uploader = FileChunkUploaderContentRange(
//        file = file,
//        uploadUrl = "https://yourserver.com/upload",
//        client = client,
//        chunkSize = 1024 * 1024 * 5, // 5MB
//        concurrency = 4
//    )
//
//    val job = uploader.upload(object : FileChunkUploaderContentRange.Callback {
//        override fun onUploadStart(totalChunks: Int) {
//            println("开始上传，共 $totalChunks 个分片")
//        }
//
//        override fun onChunkUploadStart(index: Int, offset: Long, size: Int) {
//            println("上传分片 $index 开始 (offset=$offset, size=$size)")
//        }
//
//        override fun onChunkUploadSuccess(index: Int, offset: Long, size: Int, response: HttpResponse) {
//            println("上传成功: 分片 $index 状态 ${response.status}")
//        }
//
//        override fun onChunkUploadError(index: Int, offset: Long, size: Int, throwable: Throwable) {
//            println("上传失败: 分片 $index，原因：${throwable.message}")
//        }
//
//        override fun onUploadFinished() {
//            println("所有分片上传完成")
//        }
//
//        override fun onUploadError(throwable: Throwable) {
//            println("上传过程中出现错误: ${throwable.message}")
//        }
//
//        override fun shouldContinue(): Boolean {
//            return true
//        }
//    })
//
//    delay(10000)
//    println("主动取消上传任务")
//    job.cancelAndJoin()
//}
