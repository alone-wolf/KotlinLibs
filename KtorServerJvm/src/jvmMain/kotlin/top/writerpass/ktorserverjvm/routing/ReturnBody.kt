package top.writerpass.ktorserverjvm.routing

import kotlinx.serialization.Serializable
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.http.HttpStatusCode
import top.writerpass.ktorserverjvm.routing.ApplicationCallWrapper
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.to

/**
 * 统一API响应封装类
 *
 * 为所有API端点提供标准化的响应格式，确保前端接收一致的数据结构。
 * 支持泛型，可以包装任意类型的响应数据。
 *
 * 设计原则：
 * - 统一响应格式，降低前端处理复杂度
 * - 包含完整的状态信息和错误处理
 * - 支持泛型，灵活适应不同数据类型
 * - 提供便捷的构建方式
 *
 * @param T 响应数据的类型参数，可以是任意可序列化类型
 * @property data 实际的响应数据
 * @property message 响应消息，通常为用户友好的描述
 * @property code HTTP状态码
 * @property status HTTP状态描述
 * @property success 操作是否成功的标志
 * @property error 错误信息，仅在操作失败时提供
 * @property timestamp 响应时间戳
 *
 * @author writerpass
 * @since 1.0.0
 */
@Serializable
class ReturnBody<T> @OptIn(ExperimentalTime::class) constructor(
    val data: T,
    val message: String,
    val code: Int,
    val status: String,
    val success: Boolean,
    val error: String?,
    val timestamp: Long = Clock.System.now().epochSeconds,
    val endpoint: String?
) {

    /**
     * 伴生对象，提供静态工厂方法
     *
     * 提供便捷的构建方法，避免手动创建实例的繁琐过程
     */
    companion object {

        /**
         * 创建成功响应
         *
         * @param data 响应数据
         * @param message 成功消息
         * @return 成功的ReturnBody实例，状态码为200
         */
        fun <T> success(
            data: T,
            message: String,
            endpoint: String? = null
        ): ReturnBody<T> = ReturnBody(
            code = HttpStatusCode.OK.value,
            status = HttpStatusCode.OK.description,
            message = message,
            data = data,
            success = true,
            error = null,
            endpoint = endpoint
        )

        /**
         * 创建失败响应
         *
         * @param error 技术错误消息，用于调试
         * @param message 用户友好的错误描述
         * @param statusCode HTTP状态码
         * @return 失败的ReturnBody实例
         */
        fun error(
            error: String,
            message: String,
            statusCode: HttpStatusCode,
            endpoint: String? = null
        ): ReturnBody<Unit> = ReturnBody(
            code = statusCode.value,
            status = statusCode.description,
            message = message,
            data = Unit,
            success = false,
            error = error,
            endpoint = endpoint
        )
    }

    /**
     * 检查响应是否成功
     *
     * @return 成功返回true，失败返回false
     */
    fun isSuccess(): Boolean = success

    /**
     * 检查响应是否失败
     *
     * @return 失败返回true，成功返回false
     */
    fun isError(): Boolean = !success

    /**
     * 获取响应数据或抛出异常
     *
     * 用于强制获取成功响应的数据，如果响应失败则抛出异常
     *
     * @return 响应数据
     * @throws IllegalStateException 如果响应失败，异常消息包含错误详情
     */
    fun getDataOrThrow(): T {
        if (!success) {
            throw kotlin.IllegalStateException("Response is not successful: ${error ?: message}")
        }
        return data
    }

    /**
     * 转换为Map表示
     *
     * 用于日志记录、调试和序列化场景，将响应对象转换为键值对形式
     *
     * @return 包含所有字段的Map，键名为字段名，值为对应字段值
     */
    fun toMap(): Map<String, Any?> = mapOf(
        "code" to code,
        "message" to message,
        "data" to data,
        "success" to success,
        "error" to error,
    )

    override fun toString(): String {
        return "ReturnBody(code=$code, message='$message', success=$success, error=$error)"
    }
}

/**
 * 使用示例：
 *
 * ```kotlin
 * // 成功响应示例
 * val userData = mapOf("id" to 1, "name" to "张三")
 * val successResponse = ReturnBody.success(userData, "获取用户信息成功")
 *
 * // 错误响应示例
 * val errorResponse = ReturnBody.error("用户不存在", "请求的用户ID无效", 404)
 *
 * // 使用扩展函数
 * val response = someData.toSuccessBody("处理完成")
 * ```
 */

/**
 * RoutingCall的扩展函数，提供便捷的响应方法
 */


///**
// * 发送成功响应
// *
// * Ktor应用程序调用的扩展函数，用于快速发送成功响应
// *
// * @param data 响应数据，任意可序列化类型
// * @param message 成功消息，默认为"OK"
// */
//suspend inline fun <reified T : Any> ApplicationCall.returnOk(
//    data: T,
//    message: String = HttpStatusCode.OK.description
//) = respond<ReturnBody<T>>(
//    status = HttpStatusCode.OK,
//    message = ReturnBody.success(
//        data = data,
//        message = message
//    )
//)
//
//
///**
// * 发送错误响应
// *
// * Ktor应用程序调用的扩展函数，用于快速发送错误响应
// *
// * @param message 用户友好的错误描述
// * @param statusCode HTTP状态码
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnError(
//    message: String,
//    statusCode: HttpStatusCode,
//    error: String,
//) = respond(
//    status = statusCode,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = statusCode,
//    )
//)
//
//
///**
// * 发送400 Bad Request响应
// *
// * 用于客户端请求参数错误或格式不正确的情况
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnBadRequest(
//    message: String = "请求参数错误",
//    error: String = "Bad Request"
//) = respond(
//    status = HttpStatusCode.BadRequest,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.BadRequest
//    )
//)
//
//
///**
// * 发送401 Unauthorized响应
// *
// * 用于用户未认证或认证失败的情况
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnUnauthorized(
//    message: String = "未授权访问",
//    error: String = "Unauthorized"
//) = respond(
//    status = HttpStatusCode.Unauthorized,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.Unauthorized
//    )
//)
//
//
///**
// * 发送403 Forbidden响应
// *
// * 用于用户已认证但无权限访问资源的情况
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnForbidden(
//    message: String = "访问被拒绝",
//    error: String = "Forbidden"
//) = respond(
//    status = HttpStatusCode.Forbidden,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.Forbidden
//    )
//)
//
//
///**
// * 发送404 Not Found响应
// *
// * 用于请求的资源不存在的情况
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnNotFound(
//    message: String = "请求的资源不存在",
//    error: String = "Not Found"
//) = respond(
//    status = HttpStatusCode.NotFound,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.NotFound
//    )
//)
//
//
///**
// * 发送500 Internal Server Error响应
// *
// * 用于服务器内部错误或异常情况
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnInternalError(
//    message: String = "服务器内部错误",
//    error: String = "Internal Server Error"
//) = respond(
//    status = HttpStatusCode.InternalServerError,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.InternalServerError
//    )
//)
//
//
///**
// * 发送503 Service Unavailable响应
// *
// * 用于服务器暂时不可用或维护中的情况
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnServiceUnavailable(
//    message: String = "服务暂时不可用",
//    error: String = "Service Unavailable"
//) = respond(
//    status = HttpStatusCode.ServiceUnavailable,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.ServiceUnavailable
//    )
//)
//
//
///**
// * 发送422 Unprocessable Entity响应
// *
// * 用于请求格式正确但语义错误的情况，常用于表单验证失败
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnUnprocessableEntity(
//    message: String = "请求数据验证失败",
//    error: String = "Unprocessable Entity"
//) = respond(
//    status = HttpStatusCode.UnprocessableEntity,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.UnprocessableEntity
//    )
//)
//
//
///**
// * 发送409 Conflict响应
// *
// * 用于请求与当前资源状态冲突的情况，如重复创建资源
// *
// * @param message 用户友好的错误描述
// * @param error 技术错误消息，用于调试
// */
//suspend inline fun ApplicationCall.returnConflict(
//    message: String = "请求与资源状态冲突",
//    error: String = "Conflict"
//) = respond(
//    status = HttpStatusCode.Conflict,
//    message = ReturnBody.error(
//        error = error,
//        message = message,
//        statusCode = HttpStatusCode.Conflict
//    )
//)



