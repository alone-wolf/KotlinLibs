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
//fun Application.configureSecurity() {
//    install(CSRF) {
//        // tests Origin is an expected value
//        allowOrigin("http://localhost:8080")
//
//        // tests Origin matches Host header
//        originMatchesHost()
//
//        // custom header checks
//        checkHeader("X-CSRF-Token")
//    }
//    // Please read the jwt property from the config file if you are using EngineMain
//    val jwtAudience = "jwt-audience"
//    val jwtDomain = "https://jwt-provider-domain/"
//    val jwtRealm = "ktor sample app"
//    val jwtSecret = "secret"
//    authentication {
//        jwt {
//            realm = jwtRealm
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256(jwtSecret))
//                    .withAudience(jwtAudience)
//                    .withIssuer(jwtDomain)
//                    .build()
//            )
//            validate { credential ->
//                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//            }
//        }
//    }
//    authentication {
//        val myRealm = "MyRealm"
//        val usersInMyRealmToHA1: Map<String, ByteArray> = mapOf(
//            // pass="test", HA1=MD5("test:MyRealm:pass")="fb12475e62dedc5c2744d98eb73b8877"
//            "test" to hex("fb12475e62dedc5c2744d98eb73b8877")
//        )
//
//        digest("myDigestAuth") {
//            digestProvider { userName, realm ->
//                usersInMyRealmToHA1[userName]
//            }
//        }
//    }
//    authentication {
//        basic(name = "myauth1") {
//            realm = "Ktor Server"
//            validate { credentials ->
//                if (credentials.name == credentials.password) {
//                    UserIdPrincipal(credentials.name)
//                } else {
//                    null
//                }
//            }
//        }
//
//        form(name = "myauth2") {
//            userParamName = "user"
//            passwordParamName = "password"
//            challenge {
//                /**/
//            }
//        }
//    }
//    routing {
//        authenticate("myDigestAuth") {
//            get("/protected/route/digest") {
//                val principal = call.principal<UserIdPrincipal>()!!
//                call.respondText("Hello ${principal.name}")
//            }
//        }
//        authenticate("myauth1") {
//            get("/protected/route/basic") {
//                val principal = call.principal<UserIdPrincipal>()!!
//                call.respondText("Hello ${principal.name}")
//            }
//        }
//        authenticate("myauth2") {
//            get("/protected/route/form") {
//                val principal = call.principal<UserIdPrincipal>()!!
//                call.respondText("Hello ${principal.name}")
//            }
//        }
//    }
//}
