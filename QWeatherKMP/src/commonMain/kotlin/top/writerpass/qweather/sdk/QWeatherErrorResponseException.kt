package top.writerpass.qweather.sdk

import top.writerpass.qweather.sdk.model.QWeatherErrorResponse

class QWeatherErrorResponseException1(
    val error: String
) : Exception() {
    override val message: String?
        get() = error
}

class QWeatherErrorResponseException(
    val error: QWeatherErrorResponse
) : Exception() {
    override val message: String?
        get() = error.detail
}