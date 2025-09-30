package top.writerpass.rekuester

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.date.GMTDate
import io.ktor.util.toMap
import kotlinx.serialization.json.Json


data class HttpRequestResult(
    val response: HttpResponseResult?,
    val error: String?
)

data class HttpResponseResult(
    val code: HttpStatusCode,
    val headers: Map<String, List<String>>,
    val body: String,
    val reqTime: String,
    val respTime: String
)

class RekuesterClient : AutoCloseable {
    private val client = HttpClient(CIO) {
        followRedirects = false
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(ContentEncoding) {
            deflate(1.0F)
            gzip(0.9F)
            identity()
        }
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    KermitLogger.rekuester.d(message)
                }
            }
        }
    }

    fun GMTDate.toFormatString(): String {
        return "${year}/${month.ordinal + 1}/${dayOfMonth} ${hours}:${minutes}:${seconds}.${timestamp % 1000}"
    }

    private suspend fun HttpResponse.toResult(): HttpRequestResult {
        val headers = this.headers.toMap()
        val bodyText = this.bodyAsText()
        val reqTime = this.requestTime.toFormatString()
        val respTime = this.responseTime.toFormatString()
        return HttpRequestResult(
            HttpResponseResult(
                code = this.status,
                headers = headers,
                body = bodyText,
                reqTime = reqTime,
                respTime = respTime
            ),
            null
        )
    }

    suspend fun request(
        method: HttpMethod,
        address: String,
        params: List<ApiParam> = emptyList(),
        headers: List<ApiHeader> = emptyList(),
        body: Any? = null
    ): HttpRequestResult {
        return try {
            val finalUrl = URLBuilder(address).apply {
                params.forEach {
                    parameters.append(it.key, it.value)
                }
            }.buildString()
            client.request(finalUrl) {
                this.method = method
                headers.forEach {
                    header(it.key, it.value)
                }
            }.toResult()
        } catch (e: Exception) {
            HttpRequestResult(null, e.message)
        }
    }

//    suspend fun request(api: Api): HttpResponseResult {
//        val method = api.basicInfo.method
//        val address = URLBuilder(api.basicInfo.address).buildString()
//        val headers = emptyMap<String, List<String>>()
//        return client.request(address) {
//            this.method = method
//        }.toResult()
//    }

    override fun close() {
        client.close()
    }
}