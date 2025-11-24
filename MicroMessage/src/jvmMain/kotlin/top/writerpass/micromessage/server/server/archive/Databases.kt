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
//fun Application.configureDatabases() {
//    val database = Database.connect(
//        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//        user = "root",
//        driver = "org.h2.Driver",
//        password = "",
//    )
//    val userService = UserService(database)
//    routing {
//        // Create user
//        post("/users") {
//            val user = call.receive<ExposedUser>()
//            val id = userService.create(user)
//            call.respond(HttpStatusCode.Created, id)
//        }
//
//        // Read user
//        get("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val user = userService.read(id)
//            if (user != null) {
//                call.respond(HttpStatusCode.OK, user)
//            } else {
//                call.respond(HttpStatusCode.NotFound)
//            }
//        }
//
//        // Update user
//        put("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val user = call.receive<ExposedUser>()
//            userService.update(id, user)
//            call.respond(HttpStatusCode.OK)
//        }
//
//        // Delete user
//        delete("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            userService.delete(id)
//            call.respond(HttpStatusCode.OK)
//        }
//    }
//}
