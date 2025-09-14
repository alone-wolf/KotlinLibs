package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import top.writerpass.qweather.sdk.QWeatherConfig

class IndicesApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

}