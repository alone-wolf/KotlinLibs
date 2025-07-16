package top.writerpass.ktorserverjvm

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.sse.SSE
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlinx.serialization.json.Json
import top.writerpass.ktorserverjvm.installCallLogging
import top.writerpass.ktorserverjvm.installCompression
import top.writerpass.ktorserverjvm.installShutdownEndpoint
import kotlin.time.Duration.Companion.seconds

private val commonDefaultJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun Application.installSerialization(json: Json = commonDefaultJson) {
    install(ContentNegotiation) {
        json(json)
    }
}

fun Application.installCors() {
    install(CORS) {
//        allowMethod(HttpMethod.Options)
//        allowMethod(HttpMethod.Put)
//        allowMethod(HttpMethod.Delete)
//        allowMethod(HttpMethod.Patch)
//        allowHeader(HttpHeaders.Authorization)
//        allowHeader("MyCustomHeader")
        anyHost()
        anyMethod()
    }
}



fun Application.installWebsocket() {
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(commonDefaultJson)
    }
}

fun Application.installSSE() {
    install(SSE)
}


fun Application.installCommonModules() {
    installSerialization()
    installCors()
    installCompression()
    installShutdownEndpoint()
    installCallLogging()
    installWebsocket()
    installSSE()
}

fun createServer(
    port: Int = 8080,
    host: String = "0.0.0.0",
    module: suspend Application.() -> Unit = Application::installCommonModules
): EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration> {
    return embeddedServer(
        factory = CIO,
        port = port,
        host = host,
        module = module
    )
}

fun main() {
    createServer().start(wait = true)
}