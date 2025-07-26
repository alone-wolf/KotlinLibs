package top.writerpass.ktorserverjvm.routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.webSocket

typealias RouteWrapperHandler = suspend RoutingContextWrapper.() -> Unit
typealias WsHandler = suspend DefaultWebSocketServerSession.() -> Unit

open class RouteWrapper(val route: Route) {
//    fun List<String>.get(body: RouteWrapperHandler) = forEach { get(it, body) }

    fun String.restful(
        getAll: RouteWrapperHandler? = null,
        getById: RouteWrapperHandler? = null,
        create: RouteWrapperHandler? = null,
        patch: RouteWrapperHandler? = null,
        put: RouteWrapperHandler? = null,
        delete: RouteWrapperHandler? = null
    ) {
        route {
            getAll?.let { "".get(it) }
            create?.let { "".post(it) }
            getById?.let { "/{id}".get(it) }
            patch?.let { "/{id}".patch(it) }
            put?.let { "/{id}".put(it) }
            delete?.let { "/{id}".delete(it) }
        }
    }

    fun String.get(body: RouteWrapperHandler) = get(this, body)
    fun String.post(body: RouteWrapperHandler) = post(this, body)
    fun String.delete(body: RouteWrapperHandler) = delete(this, body)
    fun String.put(body: RouteWrapperHandler) = put(this, body)
    fun String.patch(body: RouteWrapperHandler) = patch(this, body)
    fun String.route(build: RouteWrapper.() -> Unit) = route(this, build)
    fun String.websocket(handler: WsHandler) = websocket(this, handler)
    fun String.ws(handler: WsHandler) = websocket(this, handler)
}

fun RouteWrapper.get(path: String, body: RouteWrapperHandler) =
    route.get(path) { wrapper.body() }

fun RouteWrapper.post(path: String, body: RouteWrapperHandler) =
    route.post(path) { wrapper.body() }

fun RouteWrapper.delete(path: String, body: RouteWrapperHandler) =
    route.delete(path) { wrapper.body() }

fun RouteWrapper.put(path: String, body: RouteWrapperHandler) =
    route.put(path) { wrapper.body() }

fun RouteWrapper.patch(path: String, body: RouteWrapperHandler) =
    route.patch(path) { wrapper.body() }

fun RouteWrapper.route(path: String, build: RouteWrapper.() -> Unit) =
    route.route(path) { wrapper.build() }

fun RouteWrapper.websocket(path: String, handler: WsHandler) =
    route.webSocket(path, handler)

val Route.wrapper
    get() = RouteWrapper(this)