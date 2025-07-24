package top.writerpass.ktorserverjvm.routing

import io.ktor.server.routing.RoutingContext
import top.writerpass.ktorserverjvm.routing.wrapper

class RoutingContextWrapper(val context: RoutingContext) {
    val callWrapper = context.call.wrapper
}

val RoutingContext.wrapper: RoutingContextWrapper
    get() = RoutingContextWrapper(this)