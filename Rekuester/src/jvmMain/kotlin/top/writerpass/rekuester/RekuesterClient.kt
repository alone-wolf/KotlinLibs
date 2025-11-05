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
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.date.GMTDate
import io.ktor.util.toMap
import kotlinx.serialization.json.Json
import top.writerpass.kmplibrary.utils.log
import top.writerpass.rekuester.models.ApiHeader
import top.writerpass.rekuester.models.ApiParam
import top.writerpass.rekuester.models.ApiStateAuthContainer
import top.writerpass.rekuester.models.ApiStateBodyContainer
import top.writerpass.rekuester.models.AuthTypes
import kotlin.io.encoding.Base64
//import java.util.Base64
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

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

    fun URLBuilder.addParams(params: List<ApiParam>) {
        params.forEach { parameters.append(it.key, it.value) }
    }

    fun HttpMessageBuilder.addHeaders(headers: List<ApiHeader>) {
        headers.forEach { header(it.key, it.value) }
    }

    private fun buildJwtToken(jwt: ApiStateAuthContainer.Jwt): String {
        val algorithm = when (jwt.algorithm) {
            ApiStateAuthContainer.Jwt.Companion.Algorithm.HS256 -> JwtAlgorithm.HMAC256(getSecret(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.HS384 -> JwtAlgorithm.HMAC384(getSecret(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.HS512 -> JwtAlgorithm.HMAC512(getSecret(jwt))
            else -> error("Unsupported JWT algorithm: ${jwt.algorithm}")
        }

        return JWT.create()
            .withPayload(jwt.payload)
            .withHeader(parseJwtHeaders(jwt.jwtHeaders))
            .sign(algorithm)
    }

    private fun getSecret(jwt: ApiStateAuthContainer.Jwt): ByteArray =
        if (jwt.secretBase64Encoded)
            Base64.decode(jwt.secret)
        else
            jwt.secret.toByteArray()

    private fun parseJwtHeaders(json: String): Map<String, Any> {
        return try {
            kotlinx.serialization.json.Json.decodeFromString(
                kotlinx.serialization.builtins.MapSerializer(
                    kotlinx.serialization.serializer<String>(),
                    kotlinx.serialization.serializer<String>()
                ),
                json
            )
        } catch (_: Exception) {
            emptyMap()
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
            val url = URLBuilder(address).apply {
                addParams(params)
                when (auth.type) {
                    AuthTypes.ApiKey -> {
                        val key = auth.apiKey
                        if (key.addTo == ApiStateAuthContainer.ApiKey.Companion.AddTo.Param) {
                            parameters.append(key.key, key.value)
                        }
                    }

                    AuthTypes.JWT -> {
                        val jwt = auth.jwt
                        val token = buildJwtToken(jwt)
                        if (jwt.addTo == ApiStateAuthContainer.Jwt.Companion.AddTo.Param) {
                            parameters.append(
                                jwt.requestPrefix.ifBlank { "token" },
                                token
                            )
                        }
                    }

                    else -> {}
                }
            }.build()


            client.request(url) {
                this.method = method
                when (auth.type) {
                    AuthTypes.InheritAuthFromParent -> {
                        "AuthTypes.InheritAuthFromParent not implemented yet".log("RekuesterClient::request")
                    }

                    AuthTypes.NoAuth -> {}
                    AuthTypes.Basic -> {
                        val basic = auth.basic
                        if (basic != null) {
                            val credentials = "${basic.username}:${basic.password}"
                            val encoded = Base64.encode(credentials.toByteArray())
                            header(HttpHeaders.Authorization, "Basic $encoded")
                        }
                        header(
                            HttpHeaders.Authorization,
                            "Basic ${auth.basic?.username}:${auth.basic?.password}"
                        )
                    }

                    AuthTypes.Bearer -> {
                        val token = auth.bearer?.token
                        if (!token.isNullOrBlank()) {
                            header(HttpHeaders.Authorization, "Bearer $token")
                        }
                        header(HttpHeaders.Authorization, "Bearer ${auth.bearer?.token}")
                    }

                    AuthTypes.JWT -> {
                        val jwt = auth.jwt
                        if (jwt != null) {
                            val token = buildJwtToken(jwt)
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
                addHeaders(headers)
            }.toResult()
        } catch (e: Exception) {
            HttpRequestResult(null, e.message)
        }
    }

    override fun close() {
        client.close()
    }
}