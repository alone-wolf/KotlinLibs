package top.writerpass.qweather

// commonMain 源码

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import top.writerpass.qweather.ktor.clientEngine

data class QWeatherRequestResult(
    val statusCode: HttpStatusCode,
    val data: Any,
    val notImplemented: Boolean = data is String
)

class QWeatherClient(
    private val apiHost: String,
    private val apiKey: String? = null,
    private val jwtToken: String? = null
) : Closeable {
    private val client = HttpClient(clientEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(ContentEncoding) {
            deflate(1.0F)
            gzip(0.9F)
            identity()
        }

        defaultRequest {
            apiKey?.let { header("X-QW-Api-Key", it) }
            jwtToken?.let { header("Authorization", "Bearer $it") }
        }
    }

    suspend fun getNowWeather(
        location: String,
        lang: String? = "zh-hans",
        unit: String? = "m"
    ): QWeatherRequestResult {
        val url = "https://${apiHost}/v7/weather/now"
        val resp = client.get(url) {
            parameter("location", location)
            lang?.let { parameter("lang", it) }
            unit?.let { parameter("unit", it) }
        }
        val body = resp.resolve<QWeatherData.Now.ReturnBody>()
        return QWeatherRequestResult(
            statusCode = resp.status,
            data = body
        )
    }

    override fun close() {
        client.close()
    }
}

suspend inline fun <reified T : Any> HttpResponse.resolve(): Any {
    return when (status) {
        HttpStatusCode.OK -> {
            this.body<T>()
        }

        HttpStatusCode.BadRequest,
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        HttpStatusCode.TooManyRequests -> {
            this.body<QWeatherData.QWeatherReturnErrorBody>()
        }

        else -> {
            this.bodyAsText()
        }
    }
}
