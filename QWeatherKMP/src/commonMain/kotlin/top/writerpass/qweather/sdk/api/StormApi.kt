package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.StormForecastResponse
import top.writerpass.qweather.sdk.model.StormListResponse
import top.writerpass.qweather.sdk.model.StormTrackResponse
import top.writerpass.qweather.sdk.utils.check

class StormApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/tropical-cyclone/storm-forecast/
    suspend fun stormForecast(
        stormid: String,
    ): StormForecastResponse {
        return client.get("${config.baseUrl}/v7/tropical/storm-forecast") {
            url {
                parameters.append("stormid", stormid)
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/tropical-cyclone/storm-track/
    suspend fun stormTrack(
        stormid: String,
    ): StormTrackResponse {
        return client.get("${config.baseUrl}/v7/tropical/storm-track") {
            url {
                parameters.append("stormid", stormid)
            }
        }.check().body()
    }


    // https://dev.qweather.com/docs/api/tropical-cyclone/storm-list/
    suspend fun stormList(
        basin: String = "NP",
        year: String
    ): StormListResponse {
        return client.get("${config.baseUrl}/v7/tropical/storm-list") {
            url {
                parameters.append("basin", basin)
                parameters.append("year", year)
            }
        }.check().body()
    }
}