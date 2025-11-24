package top.writerpass.micromessage.server.core.data.service.user

import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import top.writerpass.micromessage.server.core.data.base.BaseRouting
import top.writerpass.micromessage.server.core.data.service.user.request.SearchQuery
import top.writerpass.micromessage.server.core.data.service.user.request.UpdateProfileRequest
import top.writerpass.micromessage.server.core.data.service.user.request.UpdateSettingsRequest

object UserRouting: BaseRouting {
    override fun apiRoutes(route: Route) {
        route.route("/users"){
            get("/me") {
                call.respondText("get current user info")
            }

            put("/me") {
                val req = call.receive<UpdateProfileRequest>()
                call.respondText("update user profile: ${req.nickname}")
            }

            patch("/me") {
                val req = call.receive<UpdateProfileRequest>()
                call.respondText("update user profile: ${req.nickname}")
            }

            post("/avatar") {
                val multipart = call.receiveMultipart()
                call.respondText("upload avatar multipart=${multipart}")
            }

            get("/{id}") {
                val id = call.parameters["id"]
                call.respondText("get user profile id=$id")
            }

            get("/settings") {
                call.respondText("get user settings")
            }

            put("/settings") {
                val req = call.receive<UpdateSettingsRequest>()
                call.respondText("update settings theme=${req.theme}")
            }

            get("/search") {
                val keyword = call.request.queryParameters["keyword"]
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
                val query = SearchQuery(keyword, page, size)
                call.respondText("search users keyword=${query.keyword}, page=${query.page}")
            }


        }
    }

    override fun adminRoutes(route: Route) {
        route.route("/user"){

        }
    }

}