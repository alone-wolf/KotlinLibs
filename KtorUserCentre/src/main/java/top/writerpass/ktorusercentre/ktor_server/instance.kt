package top.writerpass.ktorusercentre.ktor_server

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun String.log() {
    println(this)
}

private fun Application.initializer() {
    "[Init] installing Serialization".log()
    installSerialization()
    "[Init] installing CORS".log()
    installCORS()
    "[Init] installing Compression".log()
    installCompression()
    "[Init] installing ShutdownUrl".log()
    installShutdownUrl()
    "[Init] installing CallLogging".log()
    installCallLogging()
    "[Init] installing CallId".log()
    installCallID()
}

object KtorServerConfig {
    const val port = 8080
    const val host = "0.0.0.0"
}

fun ktorServer(block: Application.() -> Unit) = embeddedServer(
    factory = CIO,
    port = KtorServerConfig.port,
    host = KtorServerConfig.host
) {
    "[Init-Base] loading...".log()
    initializer()
    "[Init-Base] finished".log()
    block()
}