@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.server.core.data.service.auth

import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.bearer
import io.ktor.server.routing.Route
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.utils.PasswordUtil
import top.writerpass.micromessage.server.core.data.enums.CredentialType
import top.writerpass.micromessage.server.core.data.enums.IdentifierType
import top.writerpass.micromessage.server.core.data.service.auth.data.Credential
import top.writerpass.micromessage.server.core.data.service.auth.data.LoginSessionEntity
import top.writerpass.micromessage.server.core.data.service.auth.data.LoginSessionTable
import top.writerpass.micromessage.server.core.data.service.auth.principal.UserInfoPrincipal
import top.writerpass.micromessage.server.core.data.service.user.entity.UserIdentifierEntity
import top.writerpass.micromessage.server.core.data.service.user.table.UserIdentifierTable
import top.writerpass.micromessage.server.utils.SessionTokenGenerator
import top.writerpass.micromessage.server.utils.WithLogger
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object AuthNodes {

    interface AuthNode {
        val name: String
        val realmInfo: String
        fun AuthenticationConfig.install()
        fun Route.routeWrapper(build: Route.() -> Unit) = authenticate(
            name,
            optional = false,
            build = build
        )
    }

    object LoginUsernamePassword : AuthNode, WithLogger {

        override val name: String = "login"
        override val realmInfo: String = "Access to the '/api/v1/auth/login' path"

        override fun AuthenticationConfig.install() {
            basic(name) {
                realm = realmInfo
                validate { credentials ->

                    val username = credentials.name
                    val passwordHash0 = credentials.password

                    val principal = newSuspendedTransaction {

                        // 1. 查 identifier
                        val identifier = UserIdentifierEntity.find {
                            (UserIdentifierTable.type eq IdentifierType.Username) and
                                    (UserIdentifierTable.content eq username)
                        }.singleOrNull() ?: return@newSuspendedTransaction null

                        // 2. 查 password credential
                        val cred = Credential.Entity.find {
                            (Credential.CredentialTable.userId eq identifier.userId) and
                                    (Credential.CredentialTable.identifierId eq identifier.id.value) and
                                    (Credential.CredentialTable.type eq CredentialType.Password)
                        }.singleOrNull() ?: return@newSuspendedTransaction null

                        // 3. 校验密码
                        val calcHash = PasswordUtil.hash(passwordHash0, cred.salt)
                        if (calcHash != cred.passwordHash) return@newSuspendedTransaction null

                        // 4. 创建登陆 Session
                        val loginSession = LoginSessionEntity.new {
                            userId = identifier.userId
                            sessionToken = SessionTokenGenerator.generate()
                            expiresAt = Clock.System.now().toEpochMilliseconds() + 3600 * 1000
                        }.toData()

                        UserInfoPrincipal(
                            identifier.userId,
                            loginSession
                        )
                    }

                    principal
                }
            }
        }

        override val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)
    }

    object RefreshToken : AuthNode, WithLogger {
        override val name: String = "refresh-token"
        override val realmInfo: String = "Access to the '/api/v1/auth/refresh' path"

        override fun AuthenticationConfig.install() {
            bearer(name) {
                realm = this@RefreshToken.realmInfo
                authenticate { tokenCredential ->
                    "Authentication@refresh-login token=${tokenCredential.token}".logi()
                    if (tokenCredential.token == "asdfghjkl") {
                        UserIdPrincipal(tokenCredential.token)
                    } else {
                        null
                    }
                }
            }
        }

        override val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)
    }

    object NormalAccess : AuthNode, WithLogger {
        override val name: String = "normal-access"
        override val realmInfo: String = "Access to the normal api path"

        override fun AuthenticationConfig.install() {
            bearer(name) {
                realm = realmInfo
                authenticate { tokenCredential ->
                    val token = tokenCredential.token

                    val principal = newSuspendedTransaction {
                        val loginSession = LoginSessionEntity.find {
                            LoginSessionTable.sessionToken eq token
                        }.singleOrNull() ?: return@newSuspendedTransaction null

                        val userId = loginSession.userId

                        UserInfoPrincipal(
                            userId = userId,
                            session = loginSession.toData(),
                        )
                    }

                    principal
                }
            }
        }

        override val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)
    }
}