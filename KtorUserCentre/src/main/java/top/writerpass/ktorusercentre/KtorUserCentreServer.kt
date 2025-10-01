package top.writerpass.ktorusercentre

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import top.writerpass.ktorusercentre.database.UsersDao
import top.writerpass.ktorusercentre.database.UsersTable
import top.writerpass.ktorusercentre.database.installUsersApi
import top.writerpass.ktorusercentre.ktor_server.ktorServer
import top.writerpass.ktorusercentre.ktor_server.log


@Serializable
data class RegisterRequest(val username: String, val email: String, val passwordHash0: String)

@Serializable
data class LoginRequest(val username: String, val passwordHash0: String)

@Serializable
data class AuthResponse(val token: String)

@Serializable
data class UserResponse(val id: Long, val username: String, val email: String)

data class UserPrincipal(val username: String)

val server = ktorServer {

    Database.connect(
        url = "jdbc:sqlite:users.db",
        driver = "org.sqlite.JDBC",
    )

    transaction { SchemaUtils.create(UsersTable) }
    val jwtService = JwtService("super-secret", "ktor-auth-service")
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor-auth"
            verifier(jwtService.verifier)
            validate { credential ->
                if (credential.payload.getClaim("username").asString().isNotEmpty()) {
                    UserPrincipal(credential.payload.getClaim("username").asString())
                } else null
            }
        }
    }
    routing {
        route("/api") {
            get("/t") {
                call.request.uri.log()
                call.returnOk("")
            }
            installUsersApi()
        }
        route("/auth") {
            post("/register") {
                val req = call.receive<RegisterRequest>()
                val exists = transaction {
                    UsersDao.findByUsername(req.username).count() > 0
                }
                if (exists) {
                    call.respond(mapOf("error" to "User already exists"))
                    return@post
                }
                val hash = BCrypt.withDefaults().hashToString(12, req.passwordHash0.toCharArray())
                transaction {
                    UsersTable.insert {
                        it[username] = req.username
                        it[email] = req.email
                        it[passwordHash1] = hash
                        it[isActive] = true
                    }
                }
                call.respond(mapOf("success" to true))
            }

            post("/login") {
                val req = call.receive<LoginRequest>()
                val user = transaction {
                    UsersDao.findByUsername(req.username).singleOrNull()
                }
                if (user == null) {
                    call.respond(mapOf("error" to "Invalid credentials"))
                    return@post
                }
                val verified =
                    BCrypt.verifyer().verify(req.passwordHash0.toCharArray(), user.passwordHash1)
                if (!verified.verified) {
                    call.respond(mapOf("error" to "Invalid credentials"))
                    return@post
                }
                val token = jwtService.generateToken(user.username)
                call.respond(AuthResponse(token))
            }
        }

        authenticate("auth-jwt") {
            get("/users/me") {
                val principal = call.principal<UserPrincipal>()!!
                val user = transaction {
                    UsersDao.findByUsername(principal.username).single()
                }
                call.respond(
                    UserResponse(
                        id = user.id.value, username = user.username, email = user.email
                    )
                )
            }
        }
    }
}

fun main() {
    server.start(wait = true)
}