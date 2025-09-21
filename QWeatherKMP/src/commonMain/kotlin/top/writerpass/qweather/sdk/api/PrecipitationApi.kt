package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.PrecipitationMinutesResponse
import top.writerpass.qweather.sdk.utils.check

class PrecipitationApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {
    // https://dev.qweather.com/docs/api/minutely/minutely-precipitation/
    suspend fun weatherMinutes(
        location: String,
        lang: String? = null,
    ): PrecipitationMinutesResponse {
        return client.get("${config.baseUrl}/v7/minutely/5m") {
            url {
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
            }
        }.check().body()
    }
}