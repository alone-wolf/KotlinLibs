package top.writerpass.micromessage.server.core.data.service.auth

import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import top.writerpass.micromessage.server.core.data.base.BaseRouting
import top.writerpass.micromessage.server.core.data.service.auth.request.LoginRequest
import top.writerpass.micromessage.server.core.data.service.auth.request.RegisterRequest
import top.writerpass.micromessage.server.core.data.service.auth.request.ResetPasswordRequest

object AuthRouting: BaseRouting {
    override fun apiRoutes(route: Route) {
        route.route("/auth"){
            post("/register") {
                val req = call.receive<RegisterRequest>()
                call.respondText("register user: ${req.username}")
            }

            post("/login") {
                val req = call.receive<LoginRequest>()
                call.respondText("login with account: ${req.account}")
            }

            post("/logout") {
                val token = call.request.headers["Authorization"]
                call.respondText("logout token: $token")
            }

            post("/refresh") {
                val refreshToken = call.receive<Map<String, String>>()["refreshToken"]
                call.respondText("refresh token: $refreshToken")
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

            get("/sessions"){

            }

            delete("/sessions/{id}") {

            }
        }
    }

    override fun adminRoutes(route: Route) {

    }
}