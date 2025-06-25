package top.writerpass.ktorm.client

//import io.ktor.client.HttpClient
//import io.ktor.client.request.headers
//import io.ktor.client.request.post
//import io.ktor.client.request.setBody
//import io.ktor.client.statement.HttpResponse
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.launch
//import top.writerpass.kotlinlibrary.file.chunkReader.FileChunkReader
//import java.io.File
//
//class FileChunkUploader(
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
//        return reader.read(object : FileChunkReader.Callback {
//
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
//                    try {
//                        val response = client.post(uploadUrl) {
//                            setBody(bytes)
//                            headers {
//                                append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
//                                append("X-Chunk-Index", index.toString())
//                                append("X-Chunk-Offset", offset.toString())
//                                append("X-Chunk-Size", size.toString())
//                                append("X-File-Name", file.name)
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
