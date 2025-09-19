package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.AirQualityCurrentResponse
import top.writerpass.qweather.sdk.model.AirQualityDailyResponse
import top.writerpass.qweather.sdk.model.AirQualityHourlyResponse

class AirQualityApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/air-quality/air-current/
    suspend fun airQualityCurrent(
        latitude: String,
        longitude: String,
        lang: String? = null
    ): AirQualityCurrentResponse {
        return client.get("${config.baseUrl}/airquality/v1/current") {
            url {
                parameters.append("latitude", latitude)
                parameters.append("longitude", longitude)
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/air-quality/air-hourly-forecast/
    suspend fun airQualityHourly(
        latitude: String,
        longitude: String,
        lang: String? = null
    ): AirQualityHourlyResponse {
        return client.get("${config.baseUrl}/airquality/v1/hourly") {
            url {
                parameters.append("latitude", latitude)
                parameters.append("longitude", longitude)
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }


    // https://dev.qweather.com/docs/api/air-quality/air-daily-forecast/
    suspend fun airQualityDaily(
        latitude: String,
        longitude: String,
        lang: String? = null
    ): AirQualityDailyResponse {
        return client.get("${config.baseUrl}/airquality/v1/daily") {
            url {
                parameters.append("latitude", latitude)
                parameters.append("longitude", longitude)
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/air-quality/air-station/
//    suspend fun airQualityFromStation(
//        locationID: String,
//        lang: String? = null,
//    ): AirQualityStationResponse {
//        return client.get("${config.baseUrl}/airquality/v1/station") {
//            url {
//                appendPathSegments(locationID)
//                lang?.let { parameters.append("lang", it) }
//            }
//        }.body()
//    }


}