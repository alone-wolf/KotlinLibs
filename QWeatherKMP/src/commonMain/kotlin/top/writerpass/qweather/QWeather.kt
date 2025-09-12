package top.writerpass.qweather

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.set
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import top.writerpass.qweather.ktor.clientEngine
import top.writerpass.qweather.ktor.createHttpClient

val json = Json.parseToJsonElement(
    """{
        "name": "ChatGPT",
        "age": 3,
        "pi": 3.14159,
        "active": true
    }"""
)

//@Serializable
//class A(
//    val a: String,
//    val b: Int,
//    val c: Long,
//    val d: Double,
//    val x: D
//)
//
//@Serializable
//class D(
//    val x: Float
//)
//
//fun JsonElement.decodeAsType(type: TypeInfo) {
//    return Json.decodeFromJsonElement(type, this)
//}

suspend fun HttpClient.request(
    apiHost: String,
    endpoint: QWeatherEndpoints,
    urlRoutePath: (String) -> String,
    block: HttpRequestBuilder.() -> Unit
): QWeatherRequestResult {
    val resp = get {
        url {
            set(
                scheme = "https",
                host = apiHost,
                port = 443,
                path = urlRoutePath(endpoint.route)
            )
        }
        block()
    }
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
        urlRoutePath: (String) -> String,
        block: HttpRequestBuilder.() -> Unit
    ) = request(apiHost = apiHost, endpoint = endpoint, urlRoutePath = urlRoutePath, block = block)

    suspend fun getNowWeather(
        location: String,
        lang: String? = "zh-hans",
        unit: String? = "m"
    ) = client.request(
        endpoint = QWeatherEndpoints.Now,
        urlRoutePath = { it }
    ) {
        parameter("location", location)
        lang?.let { parameter("lang", it) }
        unit?.let { parameter("unit", it) }
    }

    suspend fun getDailyWeather(

    ) = client.request(
        endpoint = QWeatherEndpoints.Daily,
        urlRoutePath = { it.replace("{days}", "") }
    ) {

    }

    override fun close() {
        client.close()
    }
}

fun String.replaceWith(map: Map<String, String>): String {
    var a = this
    map.forEach { (key, value) ->
        a = a.replace(key, value)
    }
    return a

}

fun String.replaceWith(pair: Pair<String, String>): String {
    return replace(pair.first, pair.second)
}

