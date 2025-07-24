package top.writerpass.ktorserverjvm.routing

import io.ktor.server.routing.Routing

class RoutingWrapper(val routing: Routing) : RouteWrapper(routing)

val Routing.wrapper: RoutingWrapper
    get() = RoutingWrapper(this)