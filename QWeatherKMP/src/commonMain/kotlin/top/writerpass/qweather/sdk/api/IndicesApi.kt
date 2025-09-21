package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.IndicesResponse
import top.writerpass.qweather.sdk.utils.check

class IndicesApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/indices/indices-forecast/
    suspend fun indicesDays(
        days: String, // 1d 3d
        location: String,
        type: String, // https://dev.qweather.com/docs/resource/indices-info/
        lang: String? = null,
    ): IndicesResponse {
        return client.get("${config.baseUrl}/v7/indices") {
            url {
                appendPathSegments(days)
                parameters.append("location", location)
                parameters.append("type", type)
                lang?.let { parameters.append("lang", it) }
            }
        }.check().body()
    }

}