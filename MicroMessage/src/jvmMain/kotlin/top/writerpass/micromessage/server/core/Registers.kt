package top.writerpass.micromessage.server.core

import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.origin
import io.ktor.server.request.contentType
import io.ktor.server.request.header
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.toMap
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.core.data.service.auth.AuthNodes
import top.writerpass.micromessage.server.core.data.service.user.entity.UserEntity
import top.writerpass.micromessage.server.server.returnUnauthorized
import top.writerpass.micromessage.server.utils.WithLogger
import top.writerpass.micromessage.server.utils.logWrapper

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

//        route("/debug/dump") {
//            handle {
//                val call = call
//                val rawBody = runCatching { call.receiveText() }.getOrDefault("")
//                val isBinary = rawBody.any { it.code < 32 && it !in listOf('\n', '\r', '\t') }
//
//                val result = mapOf(
//                    "method" to call.request.httpMethod.value,
//                    "uri" to call.request.uri,
//                    "path" to call.request.path(),
//                    "headers" to call.request.headers.toMap(),
//                    "body" to if (!isBinary) rawBody else "<binary-data>"
//                )
//
//                call.respond(result)
//            }
//        }

        val routingList = Singletons.classScanner.routings
        route("/auth-api") {
            AuthNodes.LoginNicknamePassword.run {
                routeWrapper {
                    post("/login") {
                        call.principal<UserIdPrincipal>()?.let { principal ->
                            "login authorization=${principal.name}".logi()
                            call.respond("login authorization=${principal.name}")
                        } ?: call.returnUnauthorized("Unauthorized 111")

                    }
                }
            }
            AuthNodes.RefreshToken.run {
                routeWrapper {
                    post("/refresh") {
                        val principal = call.principal<UserIdPrincipal>()
                        "refresh authorization=${principal?.name}".logi()
                        call.respond("refresh authorization=${principal?.name}")
                    }
                }
            }

            @Serializable
            data class RegisterRequest(
                val username: String,
                val passwordHash0: String,
            )
            post<RegisterRequest>("/register") {
                val username = it.username
                val passwordHash0 = it.passwordHash0
                "register username=$username passwordHash0=$passwordHash0".logi()

                UserEntity.new {
                    createdAt = 0L
                }

                call.respond("register username=$username passwordHash0=$passwordHash0")
            }
        }

        AuthNodes.NormalAccess.run {
            route("/api") {
                routingList.forEach { it ->
                    it.apiRoutes(this)
                }
            }
            route("/admin-api/") {
                routingList.forEach { it ->
                    it.adminRoutes(this)
                }
            }
        }
    }
}

