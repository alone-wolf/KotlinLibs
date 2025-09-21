package top.writerpass.qweather.sdk

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import top.writerpass.qweather.ktor.clientEngine
import top.writerpass.qweather.sdk.api.AirQualityApi
import top.writerpass.qweather.sdk.api.AstronomyApi
import top.writerpass.qweather.sdk.api.GeoApi
import top.writerpass.qweather.sdk.api.HistoricalApi
import top.writerpass.qweather.sdk.api.IndicesApi
import top.writerpass.qweather.sdk.api.PrecipitationApi
import top.writerpass.qweather.sdk.api.SeaApi
import top.writerpass.qweather.sdk.api.SolarApi
import top.writerpass.qweather.sdk.api.StormApi
import top.writerpass.qweather.sdk.api.WarningApi
import top.writerpass.qweather.sdk.api.WeatherApi

object KermitLogger {
    val qweather = Logger.withTag("QWeatherClient")
}

class QWeatherClient(
    private val config: QWeatherConfig,
) : AutoCloseable {

    constructor(block: QWeatherConfigDsl.() -> Unit) : this(
        QWeatherConfigDsl().apply(block).toConfig()
    )

    private val httpClient = HttpClient(clientEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(ContentEncoding) {
            deflate(1.0F)
            gzip(0.9F)
            identity()
        }
        install(Logging) {
            level = LogLevel.INFO
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    KermitLogger.qweather.d(message)
                }
            }
        }

        defaultRequest {
            config.apiKey?.let { header("X-QW-Api-Key", it) }
            config.jwtToken?.let { header("Authorization", "Bearer $it") }
        }
    }

    val airQualityApi by lazy { AirQualityApi(httpClient, config) }
    val astronomyApi by lazy { AstronomyApi(httpClient, config) }
    val geoApi by lazy { GeoApi(httpClient, config) }
    val historicalApi by lazy { HistoricalApi(httpClient, config) }
    val indicesApi by lazy { IndicesApi(httpClient, config) }
    val precipitationApi by lazy { PrecipitationApi(httpClient, config) }
    val seaApi by lazy { SeaApi(httpClient, config) }
    val solarApi by lazy { SolarApi(httpClient, config) }
    val stormApi by lazy { StormApi(httpClient, config) }
    val warningApi by lazy { WarningApi(httpClient, config) }
    val weatherApi by lazy { WeatherApi(httpClient, config) }

    override fun close() {
        httpClient.close()
    }
}
