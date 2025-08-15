package top.writerpass.qweather

import io.ktor.http.HttpStatusCode

data class QWeatherRequestResult(
    val statusCode: HttpStatusCode,
    val data: Any,
    val notImplemented: Boolean = data is String
)