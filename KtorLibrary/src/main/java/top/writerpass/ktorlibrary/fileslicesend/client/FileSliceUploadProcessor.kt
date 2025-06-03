package top.writerpass.ktorlibrary.fileslicesend.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import top.writerpass.kotlinlibrary.coroutine.withContextResult
import top.writerpass.kotlinlibrary.utils.println
import top.writerpass.ktorlibrary.instance.getHttpClient
import top.writerpass.ktorlibrary.fileslicesend.common.InfoResponse
import top.writerpass.ktorlibrary.fileslicesend.common.PrepareRequest
import top.writerpass.ktorlibrary.fileslicesend.common.PrepareResponse

import top.writerpass.ktorlibrary.utils.contentDispositionFormFile
import java.io.File
import kotlin.coroutines.CoroutineContext

class FileSliceUploadProcessor(
    private val baseUrl: String = "http://127.0.0.1:5555/files/upload",
    private val client: HttpClient = getHttpClient(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) {
    private fun endpoint(path: String) = "${baseUrl}/${path}"

    suspend fun info(): Result<InfoResponse> {
        val url = endpoint("info")
        return withContextResult(coroutineContext) {
            val resp = client.get(url)
            resp.body<InfoResponse>()
        }
    }

    suspend fun prepare(
        file: File,
        maxFragmentSize: Long
    ): Result<PrepareResponse> {
        val url = endpoint("prepare")
        val fileSize = file.length()
        val fragmentFullNum = (fileSize / maxFragmentSize).toInt()
        val fragmentFullSize = maxFragmentSize
        val fragmentLastSize = fileSize % maxFragmentSize
        return withContextResult(coroutineContext) {
            val p = PrepareRequest(
                filename = file.name,
                size = fileSize,
                quickSend = false,
                hash = null,
                fragmentFullNum = fragmentFullNum,
                fragmentFullSize = fragmentFullSize,
                fragmentLastSize = fragmentLastSize
            )
            val resp = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(p)
            }
            resp.body<PrepareResponse>()
        }
    }

    suspend fun send(
        uuid: String,
        index: Int,
        bytes: ByteArray,
        hash: String
    ): Result<Unit> {
        val url = endpoint("send")
        "client: Step3: send, index: $index hash: $hash".println()
        return withContextResult(coroutineContext) {
            val resp = client.post(url) {
                setBody(
                    MultiPartFormDataContent(
                        parts = formData {
                            append(key = "uuid", value = uuid)
                            append(key = "index", value = index)
                            append(key = "hash", value = hash)
                            append(
                                key = "fragment",
                                value = bytes,
                                headers = headers {
                                    contentDispositionFormFile(
                                        "fragment",
                                        "fragment_${uuid}_${index}"
                                    )
                                    contentType(ContentType.Application.OctetStream)
                                }
                            )
                        },
                        boundary = "-----Session:${uuid}",
                    )
                )
//                onUpload { bytesSent, contentLength ->
//                    println("bytesSent:${bytesSent} contentLength:${contentLength}")
//                }
            }
            resp.bodyAsText().println()
        }
    }

    // /files/upload/status
    suspend fun status(): Result<Unit> {
        val url = endpoint("status")
        "on status".println()
        return withContextResult(coroutineContext) {

        }
    }

    // /files/upload/finish
    suspend fun finish(): Result<Unit> {
        val url = endpoint("finish")
        "on finish".println()
        return withContextResult(coroutineContext) {

        }
    }
}