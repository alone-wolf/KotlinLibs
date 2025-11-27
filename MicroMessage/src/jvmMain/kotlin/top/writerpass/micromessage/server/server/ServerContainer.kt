package top.writerpass.micromessage.server.server

import io.ktor.server.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.*
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.server.modules.installCallLogging
import top.writerpass.micromessage.server.server.modules.installHTTP
import top.writerpass.micromessage.server.server.modules.installLifecycleHook
import top.writerpass.micromessage.server.server.modules.installSerialization

class ServerContainer(
    config: ServerConfig = ServerConfig.default,
    private val extraModules: Application.() -> Unit = {}
) {
    val server = embeddedServer(
        factory = CIO,
        host = config.host,
        port = config.port,
        module = {
            installLifecycleHook(config.lifecycleHook)
            installHTTP()
            installSerialization()
            installCallLogging()
            extraModules()
        }
    )

    suspend fun startServerSuspend(){
        server.startSuspend(wait = true)
    }

    fun startServer(){
        server.start(true)
    }
}