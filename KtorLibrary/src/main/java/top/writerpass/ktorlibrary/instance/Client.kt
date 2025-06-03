package top.writerpass.ktorlibrary.instance

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import top.writerpass.ktorlibrary.commonDefaultJson

private val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(commonDefaultJson)
    }
}

fun getHttpClient(): HttpClient {
    return httpClient
}