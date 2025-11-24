package top.writerpass.micromessage.server.server.archive//package top.writerpass
//
//import com.auth0.jwt.JWT
//import com.auth0.jwt.algorithms.Algorithm
//import io.github.flaxoos.ktor.server.plugins.ratelimiter.*
//import io.github.flaxoos.ktor.server.plugins.ratelimiter.implementations.*
//import io.ktor.http.*
//import io.ktor.serialization.kotlinx.json.*
//import io.ktor.server.application.*
//import io.ktor.server.auth.*
//import io.ktor.server.auth.jwt.*
//import io.ktor.server.engine.*
//import io.ktor.server.html.*
//import io.ktor.server.plugins.autohead.*
//import io.ktor.server.plugins.calllogging.*
//import io.ktor.server.plugins.compression.*
//import io.ktor.server.plugins.contentnegotiation.*
//import io.ktor.server.plugins.cors.routing.*
//import io.ktor.server.plugins.csrf.*
//import io.ktor.server.plugins.defaultheaders.*
//import io.ktor.server.plugins.forwardedheaders.*
//import io.ktor.server.plugins.partialcontent.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.websocket.*
//import io.ktor.util.*
//import io.ktor.websocket.*
//import java.time.Duration
//import kotlin.time.Duration.Companion.seconds
//import kotlinx.html.*
//import kotlinx.rpc.krpc.ktor.server.Krpc
//import kotlinx.rpc.krpc.ktor.server.rpc
//import kotlinx.rpc.krpc.serialization.json.*
//import org.jetbrains.exposed.sql.*
//import org.koin.dsl.module
//import org.koin.ktor.plugin.Koin
//import org.koin.logger.slf4jLogger
//import org.slf4j.event.*
//
//fun Application.configureHTTP() {
//    install(PartialContent) {
//            // Maximum number of ranges that will be accepted from a HTTP request.
//            // If the HTTP request specifies more ranges, they will all be merged into a single range.
//            maxRangeCount = 10
//        }
//    install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
//    install(XForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
//    install(DefaultHeaders) {
//        header("X-Engine", "Ktor") // will send this header with each response
//    }
//    install(CORS) {
//        allowMethod(HttpMethod.Options)
//        allowMethod(HttpMethod.Put)
//        allowMethod(HttpMethod.Delete)
//        allowMethod(HttpMethod.Patch)
//        allowHeader(HttpHeaders.Authorization)
//        allowHeader("MyCustomHeader")
//        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
//    }
//    install(Compression)
//}
