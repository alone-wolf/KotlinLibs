package top.writerpass.rekuester

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.date.GMTDate
import io.ktor.util.encodeBase64
import io.ktor.util.toMap
import kotlinx.serialization.json.Json
import top.writerpass.kmplibrary.utils.log
import top.writerpass.rekuester.models.ApiHeader
import top.writerpass.rekuester.models.ApiParam
import top.writerpass.rekuester.models.ApiStateAuthContainer
import top.writerpass.rekuester.models.ApiStateBodyContainer
import top.writerpass.rekuester.models.AuthTypes
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes
import java.io.File

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
            level = LogLevel.ALL
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

    private fun URLBuilder.addParams(params: List<ApiParam>) {
        params.forEach { parameters.append(it.key, it.value) }
    }

    private fun HttpRequestBuilder.addHeaders(headers: List<ApiHeader>) {
        headers.forEach { header(it.key, it.value) }
    }

    private fun URLBuilder.fillAuthorizationParams(auth: ApiStateAuthContainer) {
        when (auth.type) {
            AuthTypes.ApiKey -> {
                auth.apiKey?.let { apiKey ->
                    if (apiKey.addTo == ApiStateAuthContainer.ApiKey.Companion.AddTo.Param) {
                        parameters.append(apiKey.key, apiKey.value)
                    }
                }
            }

            AuthTypes.JWT -> {
                auth.jwt?.let { jwt ->
                    val token = JwtHelper.buildJwtToken(jwt)
                    if (jwt.addTo == ApiStateAuthContainer.Jwt.Companion.AddTo.Param) {
                        parameters.append(
                            jwt.requestPrefix.ifBlank { "token" },
                            token
                        )
                    }
                }
            }

            else -> {}
        }
    }

    private fun HttpRequestBuilder.fillAuthorizationHeader(auth: ApiStateAuthContainer) {
        when (auth.type) {
            AuthTypes.InheritAuthFromParent -> {
                "AuthTypes.InheritAuthFromParent not implemented yet".log("RekuesterClient::request")
            }

            AuthTypes.NoAuth -> {}
            AuthTypes.Basic -> {
                auth.basic?.let { basic ->
                    val credentials = "${basic.username}:${basic.password}".encodeBase64()
                    header(HttpHeaders.Authorization, "Basic $credentials")
                }
            }

            AuthTypes.Bearer -> {
                auth.bearer?.let { bearer ->
                    if (bearer.token.isBlank().not()) {
                        header(HttpHeaders.Authorization, "Bearer ${bearer.token}")
                    }
                }
            }

            AuthTypes.JWT -> {
                val jwt = auth.jwt
                if (jwt != null) {
                    val token = JwtHelper.buildJwtToken(jwt)
                    if (jwt.addTo == ApiStateAuthContainer.Jwt.Companion.AddTo.Header) {
                        header(
                            HttpHeaders.Authorization,
                            "${jwt.requestPrefix.ifBlank { "Bearer" }} $token"
                        )
                    }
                }
            }

            AuthTypes.ApiKey -> {
                val key = auth.apiKey
                if (key != null && key.addTo == ApiStateAuthContainer.ApiKey.Companion.AddTo.Header) {
                    header(key.key, key.value)
                }
            }
        }
    }

    private fun HttpRequestBuilder.fillBody(body: ApiStateBodyContainer) {
        when (body.type) {
            BodyTypes.None -> {}

            BodyTypes.FormData -> {
                val data = body.formData?.filter { it.enabled } ?: emptyList()
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            for (item in data) {
                                append(item.key, item.value)
                            }
                        }
                    )
                )
            }

            BodyTypes.FormUrlencoded -> {
                val data = body.formUrlEncoded?.filter { it.enabled } ?: emptyList()
                setBody(FormDataContent(Parameters.build {
                    data.forEach { append(it.key, it.value) }
                }))
            }

            BodyTypes.Raw -> {
                body.raw?.let { (type, content) ->
                    val contentType = when (type) {
                        RawBodyTypes.Json -> ContentType.Application.Json
                        RawBodyTypes.XML -> ContentType.Application.Xml
                        RawBodyTypes.Text -> ContentType.Text.Plain
                        RawBodyTypes.Html -> ContentType.Text.Html
                        RawBodyTypes.JavaScript -> ContentType.Application.JavaScript
                        RawBodyTypes.CSS -> ContentType.Text.CSS
                    }
                    contentType(contentType)
                    setBody(content)
                }
            }

            BodyTypes.Binary -> {
                body.binary?.let { bin ->
                    val file = File(bin.path)
                    if (file.exists()) {
                        val content = file.readBytes()
                        contentType(ContentType.Application.OctetStream)
                        setBody(ByteArrayContent(content))
                    } else {
                        "Binary file not found: ${bin.path}".log("RekuesterClient::request")
                    }
                } ?: "body.binary is null".log("RekuesterClient::request")
            }

            BodyTypes.GraphQL -> {
                body.graphQL?.let { graphQL ->
                    val payload = Json.encodeToString(
                        mapOf(
                            "query" to graphQL.query,
                            "variables" to (try {
                                Json.parseToJsonElement(graphQL.variables)
                            } catch (_: Exception) {
                                Json.parseToJsonElement("{}")
                            })
                        )
                    )
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            }
        }
    }


    suspend fun request(
        method: HttpMethod,
        address: String,
        params: List<ApiParam> = emptyList(),
        headers: List<ApiHeader> = emptyList(),
        auth: ApiStateAuthContainer = ApiStateAuthContainer.Inherit,
        body: ApiStateBodyContainer = ApiStateBodyContainer.None
    ): HttpRequestResult {
        return try {
            val requestBlock: HttpRequestBuilder.() -> Unit = {
                this.method = method
                url(address) {
                    fillAuthorizationParams(auth)
                    addParams(params)
                }
                fillAuthorizationHeader(auth)
                addHeaders(headers)
                fillBody(body)
            }
            client.request(requestBlock).toResult()
        } catch (e: Exception) {
            HttpRequestResult(null, e.message)
        }
    }

    override fun close() {
        client.close()
    }
}