package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.SolarRadiationResponse
import top.writerpass.qweather.sdk.utils.check

class SolarApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/solar-radiation/solar-radiation-forecast/
    suspend fun solarRadiation(
        latitude: String,
        longitude: String,
        hours: Int = 24, // 1-60
        interval: Int = 15, // 15 30 60
        extra: String = "weather"
    ): SolarRadiationResponse {
        return client.get("${config.baseUrl}/solarradiation/v1/forecast") {
            url {
                appendPathSegments(latitude, longitude)
                parameters.append("hours", hours.toString())
                parameters.append("interval", interval.toString())
                parameters.append("extra", extra.toString())
            }
        }.check().body()
    }

}