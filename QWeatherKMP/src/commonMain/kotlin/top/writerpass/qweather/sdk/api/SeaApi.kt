package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.OceanTideResponse
import top.writerpass.qweather.sdk.utils.check

class SeaApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/ocean/tide/
    suspend fun oceanTide(
        location: String,
        date: String,
    ): OceanTideResponse {
        return client.get("${config.baseUrl}/v7/ocean/tide") {
            url {
                parameters.append("location", location)
                parameters.append("date", date)
            }
        }.check().body()
    }

}