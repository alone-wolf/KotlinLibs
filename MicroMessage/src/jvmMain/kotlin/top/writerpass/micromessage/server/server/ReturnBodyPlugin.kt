package top.writerpass.micromessage.server.server

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.response.ApplicationSendPipeline
import io.ktor.util.AttributeKey

object ReturnBodyPlugin : BaseApplicationPlugin<ApplicationCallPipeline, Unit, Unit> {

    override val key = AttributeKey<Unit>("ReturnBodyPlugin")

    override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
        val send = pipeline.sendPipeline

        // 捕获未处理异常（500）
//        pipeline.install(StatusPages) {
//            exception<Throwable> { call, cause ->
//                val r = ReturnBody.internalError(
//                    error = cause.message ?: cause.toString()
//                )
//                call.respond(HttpStatusCode.OK, r)
//            }
//        }

        // 所有响应统一包装
        send.intercept(ApplicationSendPipeline.After) { data ->
            // 1. 已经是裸 OutgoingContent（文件等）不包装
            if (data is OutgoingContent) {
                return@intercept
            }

            // 2. 已经是 ReturnBody，直接放行避免二次包裹
            if (data is ReturnBody<*>) {
                return@intercept
            }

            // 3. 如果是 HttpStatusCode → 自动变成失败响应
            if (data is HttpStatusCode) {
                val body = ReturnBody.failed(
                    statusCode = data,
                    error = data.description
                )
                proceedWith(body)
                return@intercept
            }

            // 4. 正常成功响应 → 自动封装
            val wrapped = ReturnBody.success(
                data = data,
                statusCode = HttpStatusCode.OK
            )

            proceedWith(wrapped)
        }
    }
}