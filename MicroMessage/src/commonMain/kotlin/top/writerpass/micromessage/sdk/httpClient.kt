@file:OptIn(ExperimentalAtomicApi::class)

package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object HttpClientFactory {
    fun create(
        baseUrl: String,
        authProvider: () -> String?
    ): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(prettyJson)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 5000
            }

            defaultRequest {
                url(baseUrl)
                header(HttpHeaders.Accept, "*/*")
                header(HttpHeaders.UserAgent,"MicroMessageSdk/0.0.1")
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

    suspend fun requestDebugDump(){
        val r = client.post("/debug/dump")
        val b = r.bodyAsText()
        print(b)
    }

//    val auth = AuthService(client, authStorage)
//    val user = UserService(client)
//    val message = MessageService(client)
}


//val httpClient = HttpClient(CIO) {
//    install(ContentNegotiation) {
//        json(json = prettyJson)
//    }
//}