package top.writerpass.qweather.sdk.api


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.WeatherDaysGridResponse
import top.writerpass.qweather.sdk.model.WeatherDaysResponse
import top.writerpass.qweather.sdk.model.WeatherHoursGridResponse
import top.writerpass.qweather.sdk.model.WeatherHoursResponse
import top.writerpass.qweather.sdk.model.WeatherNowGridResponse
import top.writerpass.qweather.sdk.model.WeatherNowResponse
import top.writerpass.qweather.sdk.utils.check

class WeatherApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {
    // https://dev.qweather.com/docs/api/weather/weather-now/
    suspend fun weatherNow(location: String): WeatherNowResponse {
        return client.get("${config.baseUrl}/v7/weather/now") {
            url {
                parameters.append("location", location)
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/weather/weather-daily-forecast/
    suspend fun weatherDays(
        days: String, // 3d 7d 10d 15d 30d
        location: String,
        lang: String? = null,
        unit: String? = "m",
    ): WeatherDaysResponse {
        return client.get("${config.baseUrl}/v7/weather") {
            url {
                appendPathSegments(days)
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
                unit?.let { parameters.append("unit", it) }
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/weather/weather-hourly-forecast/
    suspend fun weatherHours(
        hours: String, // 24h 72h 168h
        location: String,
        lang: String? = null,
        unit: String? = "m",
    ): WeatherHoursResponse {
        return client.get("${config.baseUrl}/v7/weather") {
            url {
                appendPathSegments(hours)
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
                unit?.let { parameters.append("unit", it) }
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/weather/grid-weather-now/
    suspend fun weatherNowGrid(
        location: String,
        lang: String? = null,
        unit: String? = "m",
    ): WeatherNowGridResponse {
        return client.get("${config.baseUrl}/v7/grid-weather/now") {
            url {
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
                unit?.let { parameters.append("unit", it) }
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/weather/grid-weather-daily-forecast/
    suspend fun weatherDaysGrid(
        days: String, // 3d 7d
        location: String,
        lang: String? = null,
        unit: String? = "m",
    ): WeatherDaysGridResponse {
        return client.get("${config.baseUrl}/v7/grid-weather") {
            url {
                appendPathSegments(days)
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
                unit?.let { parameters.append("unit", it) }
            }
        }.check().body()
    }

    // https://dev.qweather.com/docs/api/weather/grid-weather-hourly-forecast/
    suspend fun weatherHoursGrid(
        hours: String, // 24d 72d
        location: String,
        lang: String? = null,
        unit: String? = "m",
    ): WeatherHoursGridResponse {
        return client.get("${config.baseUrl}/v7/grid-weather") {
            url {
                appendPathSegments(hours)
                parameters.append("location", location)
                lang?.let { parameters.append("lang", it) }
                unit?.let { parameters.append("unit", it) }
            }
        }.check().body()
    }
}
