@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.server.core.data.service.auth

import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import top.writerpass.micromessage.server.core.data.base.BaseRouting
import top.writerpass.micromessage.server.core.data.enums.CredentialType
import top.writerpass.micromessage.server.core.data.enums.IdentifierType
import top.writerpass.micromessage.server.core.data.service.auth.data.Credential
import top.writerpass.micromessage.server.core.data.service.auth.data.LoginSessionEntity
import top.writerpass.micromessage.server.core.data.service.auth.data.LoginSessionTable
import top.writerpass.micromessage.server.core.data.service.auth.principal.UserInfoPrincipal
import top.writerpass.micromessage.server.core.data.service.auth.request.ResetPasswordRequest
import top.writerpass.micromessage.server.core.data.service.user.entity.UserEntity
import top.writerpass.micromessage.server.core.data.service.user.entity.UserIdentifierEntity
import top.writerpass.micromessage.server.core.data.service.user.table.UserIdentifierTable
import top.writerpass.micromessage.server.server.returnBadRequest
import top.writerpass.micromessage.server.server.returnConflict
import top.writerpass.micromessage.server.server.returnOk
import top.writerpass.micromessage.server.server.returnUnauthorized
import top.writerpass.micromessage.server.utils.PasswordUtil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class RegisterRequest(
    val username: String,
    val passwordHash0: String,
)

@Serializable
data class RegisterResponse(
    val userId: Long,
    val username: String,
    val createdAt: Long
)

@Serializable
data class LoginResponse(
    val token: String
)

object AuthRouting : BaseRouting {

    override fun apiRoutes(route: Route) {
        route.route("/auth") {
            // /api/v1/auth/register
            post<RegisterRequest>("/register") { req ->
                val now = Clock.System.now().toEpochMilliseconds()

                val userId = newSuspendedTransaction {
                    // 1. 检查是否有重名 username（强烈建议加）
                    val exists = UserIdentifierEntity.find {
                        (UserIdentifierTable.type eq IdentifierType.Username) and
                                (UserIdentifierTable.content eq req.username)
                    }.any()
                    if (exists) return@newSuspendedTransaction null  // null => 注册失败，外层处理

                    // 2. 创建 user
                    val user = UserEntity.new { createdAt = now }

                    // 3. 创建 identifier(username)
                    val identifier = UserIdentifierEntity.new {
                        userId = user.id.value
                        type = IdentifierType.Username
                        content = req.username
                        enabled = true
                        createdAt = now
                        updatedAt = now
                    }

                    // 4. 创建 credential
                    val saltStr = PasswordUtil.generateSalt()
                    val hash = PasswordUtil.hash(req.passwordHash0, saltStr)

                    Credential.Entity.new {
                        userId = user.id.value
                        identifierId = identifier.id.value
                        type = CredentialType.Password
                        passwordHash = hash
                        salt = saltStr
                        createdAt = now
                        updatedAt = now
                    }

                    user.id.value
                }

                // 事务返回 null ⇒ 用户名已存在
                if (userId == null) {
                    call.returnConflict("Username already exists")
                    return@post
                }

                call.returnOk(
                    RegisterResponse(
                        userId = userId,
                        username = req.username,
                        createdAt = now
                    )
                )
            }

            // /api/v1/auth/login
            AuthNodes.LoginUsernamePassword.run {
                routeWrapper {
                    post("/login") {
                        call.principal<UserInfoPrincipal>()?.let { principal ->
                            call.returnOk(LoginResponse(principal.session.sessionToken))
                        } ?: call.returnUnauthorized("Unauthorized")
                    }
                }
            }

            // /api/v1/auth/refresh
            AuthNodes.RefreshToken.run {
                routeWrapper {
                    post("/refresh") {
                        val principal = call.principal<UserIdPrincipal>()
                        "refresh authorization=${principal?.name}".logi()
                        call.respond("refresh authorization=${principal?.name}")
                    }
                }
            }

            // /api/v1/auth/sessions
            AuthNodes.NormalAccess.run {
                routeWrapper {
                    get("/sessions") {
                        call.principal<UserInfoPrincipal>()?.let { principal ->
                            val userId = principal.userId
                            val sessions = newSuspendedTransaction {
                                LoginSessionEntity.find {
                                    LoginSessionTable.userId eq userId
                                }.map { it.toData() }
                                // TODO remove token in the returns
                            }
                            call.returnOk(sessions)
                        }
                    }
                    delete("/sessions/{id}") {
                        call.principal<UserInfoPrincipal>()?.let { principal ->
                            val userId = principal.userId
                            val sessionId = call.parameters["id"]?.toLongOrNull()
                            if (sessionId == null) {
                                call.returnBadRequest("Invalid Session ID")
                                return@delete
                            }
                            newSuspendedTransaction {
                                LoginSessionEntity.findById(sessionId)?.let { session ->
                                    if (session.userId == userId) {
                                        session.delete()
                                    }
                                }
                            }
                            call.returnOk("Session deleted")
                        }
                    }
                }
            }

            post("/verify") {
                val body = call.receive<Map<String, String>>() // { "code": "123456" }
                call.respondText("verify code: ${body["code"]}")
            }

            post("/forgot-password") {
                val email = call.receive<Map<String, String>>()["email"]
                call.respondText("send reset link to: $email")
            }

            post("/change-password") {
                val req = call.receive<ResetPasswordRequest>()
                call.respondText("reset password using token: ${req.token}")
            }
        }
    }

    override fun adminRoutes(route: Route) {

    }
}