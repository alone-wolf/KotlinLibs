package top.writerpass.qweather.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class StormForecastResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val forecast: List<Forecast>,
    val refer: Refer,
) {
    @Serializable
    data class Forecast(
        val fxTime: String,
        val lat: String,
        val lon: String,
        val type: String,
        val pressure: String,
        val windSpeed: String,
        val moveSpeed: String,
        val moveDir: String,
        val move360: String,
    )
}

@Serializable
data class StormTrackResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val isActive: String,
    val now: Now?,
    val track: List<Track>,
    val refer: Refer,
) {

    @Serializable
    data class Now(
        val pubTime: String,
        val lat: String,
        val lon: String,
        val type: String,
        val pressure: String,
        val windSpeed: String,
        val moveSpeed: String,
        val moveDir: String,
        val move360: String,
        val windRadius30: WindRadius30?,
        val windRadius50: WindRadius50?,
        val windRadius64: WindRadius64?,
    )


    @Serializable
    data class Track(
        val time: String,
        val lat: String,
        val lon: String,
        val type: String,
        val pressure: String,
        val windSpeed: String,
        val moveSpeed: String,
        val moveDir: String,
        val move360: String,
        val windRadius30: WindRadius30?,
        val windRadius50: WindRadius50?,
        val windRadius64: WindRadius64?,
    )

    @Serializable
    data class WindRadius30(
        val neRadius: String?,
        val seRadius: String?,
        val swRadius: String?,
        val nwRadius: String?,
    )

    @Serializable
    data class WindRadius50(
        val neRadius: String?,
        val seRadius: String?,
        val swRadius: String?,
        val nwRadius: String?,
    )

    @Serializable
    data class WindRadius64(
        val neRadius: String?,
        val seRadius: String?,
        val swRadius: String?,
        val nwRadius: String?,
    )

}

@Serializable
data class StormListResponse(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val storm: List<Storm>,
    val refer: Refer,
) {
    
    @Serializable
    data class Storm(
        val id: String,
        val name: String,
        val basin: String,
        val year: String,
        val isActive: String,
    )
}
