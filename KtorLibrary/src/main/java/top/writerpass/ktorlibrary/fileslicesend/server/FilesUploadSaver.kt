package top.writerpass.ktorlibrary.fileslicesend.server

import io.ktor.http.content.PartData
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyAndClose
import top.writerpass.kotlinlibrary.file.utils.child
import top.writerpass.kotlinlibrary.file.utils.namedFile
import java.io.File

object FilesUploadSaver {
    private const val UPLOAD_FILE_DIR = "C:\\Users\\wolf\\Downloads"
    fun fileInScope(name: String): File {
        return UPLOAD_FILE_DIR.namedFile.child(name)
    }

    suspend fun save(destination: File, channel: ByteReadChannel) {
        channel.copyAndClose(destination.writeChannel())
    }

    suspend fun save(filename: String, source: PartData.FileItem) {
        save(
            destination = fileInScope(filename),
            channel = source.provider()
        )
    }

    fun rename(old: File, newName: String) {
        old.renameTo(fileInScope(newName))
    }
}