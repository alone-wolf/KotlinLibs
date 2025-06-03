package top.writerpass.ktorlibrary.fileslicesend.server

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.asFlow
import io.ktor.http.content.forEachPart
import io.ktor.server.plugins.CannotTransformContentToTypeException
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.cio.use
import io.ktor.util.cio.writeChannel
import io.ktor.util.collections.ConcurrentMap
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import top.writerpass.kotlinlibrary.coroutine.launch
import top.writerpass.kotlinlibrary.digest.calcSHA256String
import top.writerpass.kotlinlibrary.utils.println
import top.writerpass.ktorlibrary.fileslicesend.common.InfoResponse
import top.writerpass.ktorlibrary.fileslicesend.common.PrepareRequest
import top.writerpass.ktorlibrary.fileslicesend.common.PrepareResponse
import java.io.File
import java.util.UUID
import kotlin.collections.set
import kotlin.io.use

fun Routing.installFileSliceUpload(baseRoute: String = "/files/upload") = route(baseRoute) {
    /**
     * Step 1
     * */
    get("/info") {
        val info = InfoResponse(
            allowQuickSend = false,
            fragmentThreshold = 1024 * 1024 * 50,
            maxFragmentSize = 1024 * 1024 * 50,
            threadNum = 5
        )
        call.respond(info)
    }
    /**
     * Step 2
     * */
    post("/prepare") {
        try {
            val preparation: PrepareRequest = call.receive()
            // skip quick-upload check
            val cache = UploadCacheStore.new(
                name = preparation.filename,
                size = preparation.size,
                hash = preparation.hash,
                fragmentFullNum = preparation.fragmentFullNum,
                fragmentFullSize = preparation.fragmentFullSize,
                fragmentLastSize = preparation.fragmentLastSize
            )
            val respond = PrepareResponse(
                uuid = cache.sessionId,
                quickSendMatch = false
            )
            call.respond(respond)
        } catch (e: CannotTransformContentToTypeException) {
            e.printStackTrace()
            call.respondText(
                text = "CannotTransformContentToTypeException",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.BadRequest
            )
        }
    }

    /**
     * Step 3.1
     */
    post("/send") {
        val multiPart = call.receiveMultipart()
        var uuid: String = ""
        var hash: String = ""
        var index: Int = -1
        multiPart.asFlow()

        suspend fun MultiPartData.composeMap(onFile: suspend (PartData.FileItem) -> Unit) {
            val dataMap = ConcurrentMap<String, String>()
            multiPart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    val name = part.name ?: "null"
                    val value = part.value
                    dataMap[name] = value
                } else if (part is PartData.FileItem) {
                    onFile(part)
                }
            }
        }

        multiPart.forEachPart { part ->
            if (part is PartData.FormItem) {
                if (part.name == "index") {
                    "server: send: index:${part.value}".println()
                    index = part.value.toInt()
                } else if (part.name == "uuid") {
                    "server: send: uuid:${part.value}".println()
                    uuid = part.value
                } else if (part.name == "hash") {
                    "server: send: hash:${part.value}".println()
                    hash = part.value
                }
            } else if (part is PartData.FileItem) {
                if (part.name == "fragment") {
                    "server: send: fragment: ${part.originalFileName}".println()
                    val name = part.originalFileName as String
                    val file = File("./$name")

                    file.writeChannel(Dispatchers.IO).use {
                        part.provider().copyAndClose(this)

                        val fileHashTask = FileFragmentHashTask(
                            uuid = uuid,
                            index = index,
                            hash = hash
                        )

                        fileFragmentHashTaskFlow.emit(fileHashTask)
                    }
                }
            }
            part.dispose()
        }
        call.respondText("ok sent")
    }

    // 这个api仅允许上传一个 “file”，如果超出则返回400
//    post("/single") {
//        val obj = FilesUploadSingly()
//        val count = obj.route(this)
//        call.respondText("ok")
//    }
}

@Serializable
class MultipartItemCount(
    val fileItem: Int,
    val formItem: Int,
    val binItem: Int,
    val binChannelItem: Int
)



suspend fun PartData.FileItem.save(destination: File) {
    provider().copyAndClose(destination.writeChannel())
}

data class FileFragmentHashTask(
    val uuid: String,
    val index: Int,
    val hash: String,
) {
    var status: Char = '0' // 0: pending, 1: ok, 2: not-ok
    val filename: String
        get() = "fragment_${uuid}_${index}"
}

data class FileFragmentMergeTask(
    val uuid: String,
    val fragmentNum: Int,
    val filename: String
) {
    val files = object : Iterable<File> {
        var index = 0
        override fun iterator(): Iterator<File> {
            return object : Iterator<File> {
                override fun hasNext(): Boolean = index < fragmentNum
                override fun next(): File {
                    val f = File("./fragment_${uuid}_${index}")
                    index += 1
                    return f
                }
            }
        }

    }
}

val fileFragmentHashTaskFlow = MutableSharedFlow<FileFragmentHashTask>()

//val uploadSessionFlow = MutableSharedFlow<UploadSession>()
suspend fun runFileFragmentHashTasks(): Unit = withContext(Dispatchers.Default) {
    val semaphore = Semaphore(10)
    fileFragmentHashTaskFlow.collect { task ->
        semaphore.launch(this, Dispatchers.IO) {
            val file = File("./${task.filename}")
            val hash = file.readBytes().calcSHA256String()
            if (hash == task.hash) {
                task.status = '1'
                println("server: hash ok: ${task.filename} hash: $hash serverHash: ${task.hash}")
                // update session cache
            } else {
                task.status = '2'
                println("server: hash not ok: ${task.filename} hash: $hash serverHash: ${task.hash} HashTask: $task")
                // update session cache
            }
        }
    }
}

val fileFragmentMergeTaskFlow = MutableSharedFlow<FileFragmentMergeTask>()
suspend fun runFileFragmentMergeTasks(): Unit = withContext(Dispatchers.Default) {
    val semaphore = Semaphore(10)
    fileFragmentMergeTaskFlow.collect { task ->
        "server: merge task: $task".println()
        semaphore.launch(this, Dispatchers.IO) {
            val mergedFile = File("./${task.filename}")
            mergedFile.outputStream().use { output ->
                task.files.forEach { file ->
                    file.inputStream().use { input ->
                        output.write(input.readAllBytes())
                    }
//                    file.delete()
                }
            }
        }
    }
}


class FilesUploadSingly() {
    private var sessionId: String = ""
    private var name: String = ""
    private var size: Long = -1
    private var hash: String? = ""
    private var quickSend: Boolean = false
    private val tempFilename = "${UUID.randomUUID()}"
//    private val tempFilename = "aaaaa"

    override fun toString(): String {
        return """
            sessionId:${sessionId},\n
            name:${name},\n
            size:${size},\n
            hash:${hash},\n
            quickSend:${quickSend}
        """.trimIndent()
    }

    suspend fun route(context: RoutingContext): MultipartItemCount {
        val multiPart = context.call.receiveMultipart()
        val count = multiPart.consume()
        assert(sessionId != "")
        FilesUploadSaver.rename(
            FilesUploadSaver.fileInScope(tempFilename),
            sessionId
        )
        return count
    }

    private suspend fun MultiPartData.consume(): MultipartItemCount {
        var fileNum = 0
        var formItemNum = 0
        var binaryChannelItemNum = 0
        var binaryItemNum = 0

        forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "sessionId" -> sessionId = part.value
                        "name" -> name = part.value
                        "size" -> size = part.value.toLong()
                        "hash" -> hash = part.value
                        "quickSend" -> quickSend = part.value.toBoolean()
                    }
                    formItemNum += 1
                }

                is PartData.FileItem -> {
                    FilesUploadSaver.save(tempFilename, part)
                    fileNum += 1
                }

                is PartData.BinaryItem -> {
                    binaryItemNum += 1
                }

                is PartData.BinaryChannelItem -> {
                    binaryChannelItemNum += 1
                }
            }
            part.dispose()
        }

        return MultipartItemCount(
            fileItem = fileNum,
            formItem = formItemNum,
            binItem = binaryItemNum,
            binChannelItem = binaryChannelItemNum
        )
    }
}