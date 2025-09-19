package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.HistoricalAirResponse
import top.writerpass.qweather.sdk.model.HistoricalWeatherResponse

class HistoricalApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/time-machine/time-machine-weather/
    suspend fun historicalWeather(
        location: String,
        date: String,
        lang: String? = null,
        unit: String = "m"
    ): HistoricalWeatherResponse {
        return client.get("${config.baseUrl}/v7/historical/weather") {
            url {
                parameters.append("location", location)
                parameters.append("date", date)
                lang?.let { parameters.append("lang", it) }
                parameters.append("unit", unit)
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/time-machine/time-machine-air/
    suspend fun historicalAir(
        location: String,
        date: String,
        lang: String? = null,
    ): HistoricalAirResponse {
        return client.get("${config.baseUrl}/v7/historical/air") {
            url {
                parameters.append("location", location)
                parameters.append("date", date)
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }
}