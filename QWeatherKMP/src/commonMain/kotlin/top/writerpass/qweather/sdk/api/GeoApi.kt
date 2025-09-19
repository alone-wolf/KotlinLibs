package top.writerpass.qweather.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import top.writerpass.qweather.sdk.QWeatherConfig
import top.writerpass.qweather.sdk.model.GeoCityLookupResponse
import top.writerpass.qweather.sdk.model.GeoCityTopResponse
import top.writerpass.qweather.sdk.model.GeoPOILookupResponse
import top.writerpass.qweather.sdk.model.GeoPOIRangeResponse

class GeoApi(
    private val client: HttpClient,
    private val config: QWeatherConfig
) {

    // https://dev.qweather.com/docs/api/geoapi/city-lookup/
    suspend fun cityLookup(
        location: String,
        adm: String? = null,
        range: String? = null,
        number: Int? = 10,
        lang: String? = null
    ): GeoCityLookupResponse {
        return client.get("${config.baseUrl}/geo/v2/city/lookup") {
            url {
                parameters.append("location", location)
                adm?.let { parameters.append("adm", it) }
                range?.let { parameters.append("range", it) }
                number?.let { parameters.append("number", it.toString()) }
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/geoapi/top-city/
    suspend fun cityTop(
        range: String? = null,
        number: Int? = 10,
        lang: String? = null
    ): GeoCityTopResponse {
        return client.get("${config.baseUrl}/geo/v2/city/top") {
            url {
                range?.let { parameters.append("range", it) }
                number?.let { parameters.append("number", it.toString()) }
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }


    // https://dev.qweather.com/docs/api/geoapi/poi-lookup/
    suspend fun poiLookup(
        location: String,
        type: String, // scenic 景点, CSTA 潮流站点, TSTA 潮汐站点
        city: String? = null,
        number: Int? = 10,
        lang: String? = null,
    ): GeoPOILookupResponse {
        return client.get("${config.baseUrl}/geo/v2/poi/lookup") {
            url {
                parameters.append("location", location)
                parameters.append("type", type)
                city?.let { parameters.append("city", it) }
                number?.let { parameters.append("number", it.toString()) }
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }

    // https://dev.qweather.com/docs/api/geoapi/poi-range/
    suspend fun poiRange(
        location: String,
        type: String, // scenic 景点, CSTA 潮流站点, TSTA 潮汐站点
        radius: Int? = 5,
        number: Int? = 10,
        lang: String? = null,
    ): GeoPOIRangeResponse {
        return client.get("${config.baseUrl}/geo/v2/poi/range") {
            url {
                parameters.append("location", location)
                parameters.append("type", type)
                radius?.let { parameters.append("radius", it.toString()) }
                number?.let { parameters.append("number", it.toString()) }
                lang?.let { parameters.append("lang", it) }
            }
        }.body()
    }
}


