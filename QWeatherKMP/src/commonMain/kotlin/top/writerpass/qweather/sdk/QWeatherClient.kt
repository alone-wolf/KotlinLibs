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

class QWeatherClient(private val config: QWeatherConfig) : AutoCloseable {

    constructor(block: QWeatherConfigDsl.() -> Unit) : this(
        QWeatherConfigDsl().apply(block).toConfig()
    )

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

    //    suspend fun close() = httpClient.close()
    override fun close() {
        httpClient.close()
    }
}
