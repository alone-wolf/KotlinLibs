package top.writerpass.qweather

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.set
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.core.Closeable
import top.writerpass.qweather.ktor.clientEngine
import top.writerpass.qweather.ktor.createHttpClient

suspend fun HttpClient.request(
    apiHost: String,
    endpoint: QWeatherEndpoints,
    block: HttpRequestBuilder.(String) -> Unit
): QWeatherRequestResult {
    val url = endpoint.getUrl(apiHost)
    val resp = get { block(url) }
    val body = resp.resolve(endpoint.returnBodyTypeInfo)
    return QWeatherRequestResult(
        statusCode = resp.status,
        data = body
    )
}

suspend fun HttpResponse.resolve(typeInfo: TypeInfo): Any {
    return when (status) {
        HttpStatusCode.OK -> body(typeInfo)

        HttpStatusCode.BadRequest,
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        HttpStatusCode.TooManyRequests -> body<QWeatherData.QWeatherReturnErrorBody>()

        else -> bodyAsText()
    }
}

class QWeatherClient(
    private val apiHost: String,
    private val apiKey: String? = null,
    private val jwtToken: String? = null
) : Closeable {
    private val client = createHttpClient(clientEngine) {
        defaultRequest {
            apiKey?.let { header("X-QW-Api-Key", it) }
            jwtToken?.let { header("Authorization", "Bearer $it") }
        }
    }

    private suspend fun HttpClient.request(
        endpoint: QWeatherEndpoints,
        block: HttpRequestBuilder.(String) -> Unit
    ) = request(apiHost = apiHost, endpoint = endpoint, block = block)

    suspend fun getNowWeather(
        location: String,
        lang: String? = "zh-hans",
        unit: String? = "m"
    ): QWeatherRequestResult = client.request(QWeatherEndpoints.Now) { urlString ->
        url { set("https", apiHost, 443) }
        url(urlString)
        parameter("location", location)
        lang?.let { parameter("lang", it) }
        unit?.let { parameter("unit", it) }
    }

    override fun close() {
        client.close()
    }
}


