@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.server.core

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.core.data.service.auth.AuthNodes
import top.writerpass.micromessage.server.utils.WithLogger
import top.writerpass.micromessage.server.utils.logWrapper
import kotlin.time.ExperimentalTime


@Serializable
data class DebugDump(
    val method: String,
    val uri: String,
    val path: String,

    // Query 是多值 Map
    val queryParameters: Map<String, List<String>>,

    // Headers 也是多值 Map
    val headers: Map<String, List<String>>,

    // Cookies map
    val cookies: Map<String, String>,

    val clientIP: String?,
    val scheme: String,

    // origin host info
    val host_local: String?,
    val host_server: String?,
    val port_local: Int,
    val port_server: Int,

    val protocol: String,
    val contentType: String,
    val contentLength: Long?,

    // body 一律用 String 容器
    val body: String
)


class Register : WithLogger {
    override val logger: Logger = LoggerFactory.getLogger(Register::class.simpleName)

    fun registerTables() {
        val database: Database = Singletons.databaseContainer.database
        logWrapper("register database tables") {
            transaction(database) {
                SchemaUtils.create(
                    *Singletons.classScanner.tables
                )
            }
        }
    }

    fun registerServiceModules(application: Application) {
        application.routing {
            registerRoutes()
        }.let {
            logWrapper("list route") {
                it.getAllRoutes().forEach { route ->
                    "Route: $route".logi()
                }
            }
        }
    }

    fun Routing.registerRoutes() {
        debugDumpRoute()
        route("/api") {
            route("/v1") {
                val routingList = Singletons.classScanner.routings
                routingList.forEach { routingItem ->
                    routingItem.apiRoutes(this)
                }
                AuthNodes.NormalAccess.run {
                    route("/admin/") {
                        routingList.forEach { routingItem ->
                            routingItem.adminRoutes(this)
                        }
                    }
                }
            }
        }
    }
}

fun Routing.debugDumpRoute() {
    route("/debug/dump") {
        handle {
            val call = call

            // 读取 Body（自动处理 JSON / form / raw）
            val rawBody = runCatching { call.receiveText() }.getOrDefault("")

            val isBinary = rawBody.any { it.code < 32 && it !in listOf('\n', '\r', '\t') }

            val response = DebugDump(
                method = call.request.httpMethod.value,
                uri = call.request.uri,
                path = call.request.path(),

                queryParameters = call.request.queryParameters.toMap(),
                headers = call.request.headers.entries().associate { it.key to it.value },
                cookies = call.request.cookies.rawCookies,

                clientIP = call.request.origin.remoteHost,
                scheme = call.request.origin.scheme,

                host_local = call.request.origin.localHost,
                host_server = call.request.origin.serverHost,
                port_local = call.request.origin.localPort,
                port_server = call.request.origin.serverPort,

                protocol = call.request.origin.version,
                contentType = call.request.contentType().toString(),
                contentLength = call.request.header(HttpHeaders.ContentLength)?.toLongOrNull(),

                body = if (!isBinary) rawBody else "<binary-data (${rawBody.length} bytes)>"
            )

            call.respond(response)
        }
    }
}

