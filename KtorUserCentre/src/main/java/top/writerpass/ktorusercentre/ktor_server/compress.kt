package top.writerpass.ktorusercentre.ktor_server

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression

internal fun Application.installCompression() {
    install(Compression)
}