package top.writerpass.qweather.ktor

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect val clientEngine: HttpClientEngineFactory<*>

fun <T : HttpClientEngineConfig> createHttpClient(
    engineFactory: HttpClientEngineFactory<T>,
    block: HttpClientConfig<T>.() -> Unit
): HttpClient {
    return HttpClient(engineFactory) {
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
        block()
    }
}