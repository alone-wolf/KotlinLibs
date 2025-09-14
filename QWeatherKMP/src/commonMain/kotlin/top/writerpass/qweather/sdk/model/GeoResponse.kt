package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoCityLookupResponse(
    val code: String,
    val location: List<Location>,
    val refer: Refer,
) {
    @Serializable
    data class Location(
        val name: String,
        val id: String,
        val lat: String,
        val lon: String,
        val adm2: String,
        val adm1: String,
        val country: String,
        val tz: String,
        val utcOffset: String,
        val isDst: String,
        val type: String,
        val rank: String,
        val fxLink: String,
    )
}


@Serializable
data class GeoCityTopResponse(
    val code: String,
    val topCityList: List<TopCityList>,
    val refer: Refer,
) {
    @Serializable
    data class TopCityList(
        val name: String,
        val id: String,
        val lat: String,
        val lon: String,
        val adm2: String,
        val adm1: String,
        val country: String,
        val tz: String,
        val utcOffset: String,
        val isDst: String,
        val type: String,
        val rank: String,
        val fxLink: String,
    )
}

@Serializable
data class GeoPOILookupResponse(
    val code: String,
    val poi: List<Poi>,
    val refer: Refer,
) {
    @Serializable
    data class Poi(
        val name: String,
        val id: String,
        val lat: String,
        val lon: String,
        val adm2: String,
        val adm1: String,
        val country: String,
        val tz: String,
        val utcOffset: String,
        val isDst: String,
        val type: String,
        val rank: String,
        val fxLink: String,
    )
}

@Serializable
data class GeoPOIRangeResponse(
    val code: String,
    val poi: List<GeoPOILookupResponse.Poi>,
    val refer: Refer,
)