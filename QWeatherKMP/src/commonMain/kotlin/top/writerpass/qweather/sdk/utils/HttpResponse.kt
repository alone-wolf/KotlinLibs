package top.writerpass.qweather.sdk.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import top.writerpass.qweather.sdk.QWeatherErrorResponseException1

suspend fun HttpResponse.check(): HttpResponse {
    if (this.status != HttpStatusCode.OK) {
        throw QWeatherErrorResponseException1(bodyAsText())
    } else {
        return this
    }
}