package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.AstronomyMoonResponse
import top.writerpass.qweather.sdk.model.AstronomySunElevationAngleResponse
import top.writerpass.qweather.sdk.model.AstronomySunResponse

class AstronomyApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/astronomy/sunrise-sunset/
    suspend fun astronomySun(
        location: String,
        date: String,
    ): AstronomySunResponse {
        return client.get("${config.baseUrl}/v7/astronomy/sun") {
            url {
                parameters.append("location", location)
                parameters.append("date", date)
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/astronomy/moon-and-moon-phase/
    suspend fun astronomyMoon(
        location: String,
        date: String,
    ): AstronomyMoonResponse {
        return client.get("${config.baseUrl}/v7/astronomy/moon") {
            url {
                parameters.append("location", location)
                parameters.append("date", date)
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/astronomy/solar-elevation-angle/
    suspend fun astronomySunElevationAngle(
        location: String,
        date: String,
        time: String, // HHmm
        tz: String,
        alt: Int
    ): AstronomySunElevationAngleResponse {
        return client.get("${config.baseUrl}/v7/astronomy/solar-elevation-angle") {
            url {
                parameters.append("location", location)
                parameters.append("date", date)
                parameters.append("time", time)
                parameters.append("tz", tz)
                parameters.append("alt", alt.toString())
            }
        }.body()
    }
}