package top.writerpass.ktorserverjvm

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression

fun Application.installCompression() {
    install(Compression)
}