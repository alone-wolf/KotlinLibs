package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.WarningListResponse
import top.writerpass.qweather.sdk.model.WarningNowResponse
import top.writerpass.qweather.sdk.utils.check

class WarningApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {
    // https://dev.qweather.com/docs/api/warning/weather-warning/
    suspend fun warningNow(
        location: String,
        lang: String? = null,
    ): WarningNowResponse {
        return client.get("${config.baseUrl}/v7/warning/now") {
            url {
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/warning/weather-warning-city-list/
    suspend fun warningList(
        range: String,
    ): WarningListResponse {
        return client.get("${config.baseUrl}/v7/warning/list") {
            url {
                parameters.append("range", range)
            }
        }.check().body()
    }
}