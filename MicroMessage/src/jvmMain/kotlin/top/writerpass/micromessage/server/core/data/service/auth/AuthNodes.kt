package top.writerpass.micromessage.server.core.data.service.auth

import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.BearerTokenCredential
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.bearer
import io.ktor.server.request.path
import io.ktor.server.routing.Route
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.core.data.service.auth.data.LoginSession

class UserInfoPrincipal(
    val userId: Long,
    val session: LoginSession.Data,
)

object AuthNodes {
    interface AuthNode<Credential> {
        val name: String
        val realmInfo: String
        fun AuthenticationConfig.install()
        fun Route.routeWrapper(build: Route.() -> Unit) = authenticate(
            LoginNicknamePassword.name,
            optional = false,
            build = build
        )
    }

    private val logger = LoggerFactory.getLogger("Authentication")
    private fun String.logi() = logger.info(this)

    object LoginNicknamePassword : AuthNode<UserPasswordCredential> {

        override val name: String = "login"
        override val realmInfo: String = "Access to the '/auth-api/login' path"

        override fun AuthenticationConfig.install() {
            basic(name) {
                realm = realmInfo
                validate { credentials ->
                    // 根据所谓用户名、密码从数据库查询用户信息
                    // 根据用户信息创建新的Session，包含token、refresh-token并包含在UserPrincipal中传给route
                    // login的route将token、refresh-token返回给用户
                    val nickname = credentials.name
                    val password = credentials.password
                    "Authentication@login name=${nickname} password=${password}".logi()
                    if (credentials.name == "admin" && credentials.password == "password") {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
            }
        }
    }

    object RefreshToken : AuthNode<BearerTokenCredential> {
        override val name: String = "refresh-token"
        override val realmInfo: String = "Access to the '/auth-api/refresh' path"

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

    }

    object NormalAccess : AuthNode<BearerTokenCredential> {
        override val name: String = "normal-access"
        override val realmInfo: String = "Access to the '/api/*' path"

        override fun AuthenticationConfig.install() {
            bearer(name) {
                realm = realmInfo
                authenticate { tokenCredential ->
                    val token = tokenCredential.token
                    val path = this.request.path()

                    LoginSession.Entity.find {
                        LoginSession.Table.sessionToken eq token
                    }.singleOrNull()?.let { session ->
                        UserInfoPrincipal(
                            session.userId.value,
                            session.toData(),
                        )
                        "Authentication@normal-access token=${tokenCredential.token}".logi()
                        if (token == "asdfghjkl") {
                            UserIdPrincipal(tokenCredential.token)
                        }
                    }

//                    AuthService.authCheckWithToken(token)?.let { session ->
//                        UserInfoPrincipal(
//                            session.userId.value,
//                            session.toData(),
//                        )
//                        "Authentication@normal-access token=${tokenCredential.token}".logi()
//                        if (token == "asdfghjkl") {
//                            UserIdPrincipal(tokenCredential.token)
//                        } else {
//                            null
//                        }
//                    }
                }
            }
        }
    }
}