package top.writerpass.qweather.sdk

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import top.writerpass.qweather.sdk.api.WeatherApi

class QWeatherClient(private val config: QWeatherConfig) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
        install(ContentEncoding) {
            deflate(1.0F)
            gzip(0.9F)
            identity()
        }
        defaultRequest {
            config.apiKey?.let { header("X-QW-Api-Key", it) }
            config.jwtToken?.let { header("Authorization", "Bearer $it") }
        }
    }

    val weatherApi by lazy { WeatherApi(httpClient, config) }
//    val geoApi = GeoApi(httpClient, config)

    suspend fun close() = httpClient.close()
}
