package top.writerpass.ktorserverjvm.routing

import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.request.uri
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import java.io.File

@JvmInline
value class ApplicationCallWrapper(val call: ApplicationCall) {
    suspend inline fun <reified T : Any> receive() = call.receive<T>()
}


val ApplicationCall.wrapper: ApplicationCallWrapper
    get() = ApplicationCallWrapper(this)

suspend inline fun <reified T : Any> ApplicationCallWrapper.returnWithStatus(
    data: T,
    message: String = HttpStatusCode.OK.description
) = call.respond<ReturnBody<T>>(
    status = HttpStatusCode.OK,
    message = ReturnBody.success(
        data = data,
        message = message,
        endpoint = call.request.uri
    )
)


suspend inline fun ApplicationCallWrapper.returnFile(file: File) {
    val thisFile = file.canonicalFile
    if (thisFile.exists().not()) {
        returnNotFound()
    } else if (thisFile.isFile.not()) {
        returnNotFound()
    } else {
        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(
                ContentDisposition.Parameters.FileName,
                thisFile.name
            ).toString()
        )
        call.respondFile(thisFile)
    }
}

suspend inline fun <reified T : Any> ApplicationCallWrapper.returnOk(
    data: T,
    message: String = HttpStatusCode.OK.description
) = call.respond<ReturnBody<T>>(
    status = HttpStatusCode.OK,
    message = ReturnBody.success(
        data = data,
        message = message,
        endpoint = call.request.uri
    )
)

suspend inline fun <reified T : Any> ApplicationCallWrapper.returnCreated(
    data: T,
    message: String = HttpStatusCode.Created.description
) = call.respond<ReturnBody<T>>(
    status = HttpStatusCode.Created,
    message = ReturnBody(
        data = data,
        message = message,
        code = HttpStatusCode.Created.value,
        status = HttpStatusCode.Created.description,
        success = true,
        error = null,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnError(
    message: String,
    statusCode: HttpStatusCode,
    error: String,
) = call.respond(
    status = statusCode,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = statusCode,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnBadRequest(
    message: String = "请求错误",
    error: String = "Bad Request"
) = call.respond(
    status = HttpStatusCode.BadRequest,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.BadRequest,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnUnauthorized(
    message: String = "未授权访问",
    error: String = "Unauthorized"
) = call.respond(
    status = HttpStatusCode.Unauthorized,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.Unauthorized,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnForbidden(
    message: String = "访问被拒绝",
    error: String = "Forbidden"
) = call.respond(
    status = HttpStatusCode.Forbidden,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.Forbidden,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnNotFound(
    message: String = "请求的资源不存在",
    error: String = "Not Found"
) = call.respond(
    status = HttpStatusCode.NotFound,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.NotFound,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnInternalError(
    message: String = "服务器内部错误",
    error: String = "Internal Server Error"
) = call.respond(
    status = HttpStatusCode.InternalServerError,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.InternalServerError,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnServiceUnavailable(
    message: String = "服务暂时不可用",
    error: String = "Service Unavailable"
) = call.respond(
    status = HttpStatusCode.ServiceUnavailable,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.ServiceUnavailable,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnUnprocessableEntity(
    message: String = "请求数据验证失败",
    error: String = "Unprocessable Entity"
) = call.respond(
    status = HttpStatusCode.UnprocessableEntity,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.UnprocessableEntity,
        endpoint = call.request.uri
    )
)

suspend inline fun ApplicationCallWrapper.returnConflict(
    message: String = "请求与资源状态冲突",
    error: String = "Conflict"
) = call.respond(
    status = HttpStatusCode.Conflict,
    message = ReturnBody.error(
        error = error,
        message = message,
        statusCode = HttpStatusCode.Conflict,
        endpoint = call.request.uri
    )
)
