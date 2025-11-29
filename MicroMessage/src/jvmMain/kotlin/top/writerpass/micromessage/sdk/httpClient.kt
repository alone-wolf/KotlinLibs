@file:OptIn(ExperimentalAtomicApi::class)

package top.writerpass.micromessage.sdk

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.common.ServerRoutes
import top.writerpass.micromessage.common.request.RegisterRequest
import top.writerpass.micromessage.common.utils.WithLogger
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object HttpClientFactory {
    fun create(
        baseUrl: String,
        authProvider: () -> String?
    ): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    explicitNulls = false
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 5000
            }

            defaultRequest {
                url(baseUrl)
                header(HttpHeaders.Accept, "*/*")
                header(HttpHeaders.UserAgent, "MicroMessageSdk/0.0.1")
                authProvider()?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.value >= 300) {
                        val text = response.bodyAsText()
                        throw ApiException(response.status, text)
                    }
                }
            }
        }
    }
}

class ApiException(status: HttpStatusCode, text: String) : Throwable() {

}

interface AuthStorage {
    fun saveToken(token: String)
    fun getToken(): String
}

class InMemoryAuthStorage : AuthStorage {
    private var value = AtomicReference("")
    override fun saveToken(token: String) {
        value.store(token)
    }

    override fun getToken(): String {
        return value.load()
    }
}

class ApiClient(
    baseUrl: String,
    private val authStorage: AuthStorage = InMemoryAuthStorage()
) {
    private val client = HttpClientFactory.create(
        baseUrl = baseUrl,
        authProvider = { authStorage.getToken() }
    )

    suspend fun requestDebugDump() {
        val r = client.post("/debug/dump")
        val b = r.bodyAsText()
        print(b)
    }

    val auth = AuthService(client)
//    val user = UserService(client)
//    val message = MessageService(client)
}

class AuthService(private val client: HttpClient) : WithLogger {
    suspend fun register(username: String, password: String) {
        val passwordHash0 = password
        val reqBody = RegisterRequest(username, passwordHash0)
        val r = client.post(ServerRoutes.Api.V1.Auth.Register.path) {
            contentType(ContentType.Application.Json)
            setBody(reqBody)
        }
        val b = r.bodyAsText()
        print(b)
    }

    override val logger: Logger = LoggerFactory.getLogger(this::class.java)
}


//val httpClient = HttpClient(CIO) {
//    install(ContentNegotiation) {
//        json(json = prettyJson)
//    }
//}