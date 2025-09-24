@file:OptIn(ExperimentalTime::class)

package top.writerpass.ktorusercentre


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
class ReturnBody<T> private constructor(
    val code: Int,
    val message: String,
    val data: T?,
    val error: String?
) {
    companion object {
        fun <T> success(
            data: T?,
            statusCode: HttpStatusCode = HttpStatusCode.OK,
            message: String? = null
        ) = ReturnBody(
            code = statusCode.value,
            message = message ?: statusCode.description,
            data = data,
            error = null
        )

        fun failed(
            statusCode: HttpStatusCode,
            message: String? = null,
            error: String
        ) = ReturnBody(
            code = statusCode.value,
            message = message ?: statusCode.description,
            data = null,
            error = error
        )

        // --------- 常见 HTTP 状态码快捷方法 ---------
        // 200 OK
        inline fun <reified T> ok(data: T? = null, message: String? = null) =
            success(data, HttpStatusCode.OK, message)

        // 201 Created
        inline fun <reified T> created(data: T? = null, message: String? = null) =
            success(data, HttpStatusCode.Created, message)

        // 400 Bad Request
        fun badRequest(error: String, message: String? = null) =
            failed(HttpStatusCode.BadRequest, message, error)

        // 401 Unauthorized
        fun unauthorized(error: String, message: String? = null) =
            failed(HttpStatusCode.Unauthorized, message, error)

        // 403 Forbidden
        fun forbidden(error: String, message: String? = null) =
            failed(HttpStatusCode.Forbidden, message, error)

        // 404 Not Found
        fun notFound(error: String, message: String? = null) =
            failed(HttpStatusCode.NotFound, message, error)

        // 500 Internal Server Error
        fun internalError(error: String, message: String? = null) =
            failed(HttpStatusCode.InternalServerError, message, error)

        // 502 Bad Gateway
        fun badGateway(error: String, message: String? = null) =
            failed(HttpStatusCode.BadGateway, message, error)

        // 503 Service Unavailable
        fun serviceUnavailable(error: String, message: String? = null) =
            failed(HttpStatusCode.ServiceUnavailable, message, error)
    }
}

suspend inline fun <reified T> ApplicationCall.returnOk(data: T, message: String? = null) =
    respond(
        status = HttpStatusCode.OK,
        message = ReturnBody.ok(data, message)
    )

suspend inline fun <reified T> ApplicationCall.returnCreated(
    data: T,
    message: String? = null
) =
    respond(
        status = HttpStatusCode.Created,
        message = ReturnBody.created(data, message)
    )

suspend fun ApplicationCall.returnBadRequest(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.BadRequest,
        message = ReturnBody.badRequest(error, message)
    )

suspend fun ApplicationCall.returnUnauthorized(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.Unauthorized,
        message = ReturnBody.unauthorized(error, message)
    )

suspend fun ApplicationCall.returnForbidden(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.Forbidden,
        message = ReturnBody.forbidden(error, message)
    )

suspend fun ApplicationCall.returnNotFound(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.NotFound,
        message = ReturnBody.notFound(error, message)
    )

suspend fun ApplicationCall.returnInternalError(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.InternalServerError,
        message = ReturnBody.internalError(error, message)
    )

suspend fun ApplicationCall.returnBadGateway(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.BadGateway,
        message = ReturnBody.badGateway(error, message)
    )

suspend fun ApplicationCall.returnServiceUnavailable(error: String, message: String? = null) =
    respond(
        status = HttpStatusCode.ServiceUnavailable,
        message = ReturnBody.serviceUnavailable(error, message)
    )



