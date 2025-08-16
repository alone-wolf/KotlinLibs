package top.writerpass.qweather

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

sealed class QWeatherEndpoints(
    val route: String,
    val returnBodyTypeInfo: TypeInfo
) {
//    fun getUrl(apiHost: String): String {
//        return "https://${apiHost}${route}"
//    }

    object Now : QWeatherEndpoints(
        route = "/v7/weather/now",
        returnBodyTypeInfo = typeInfo<QWeatherData.Now.ReturnBody>()
    )

    object Daily : QWeatherEndpoints(
        route = "/v7/weather/{days}",
        returnBodyTypeInfo = typeInfo<QWeatherData.Daily.ReturnBody>()
    )

    object Hourly : QWeatherEndpoints(
        route = "/v7/weather/{hours}",
        returnBodyTypeInfo = typeInfo<QWeatherData.Hourly.ReturnBody>()
    )

    object GridNow : QWeatherEndpoints(
        route = "/v7/grid-weather/now",
        returnBodyTypeInfo = typeInfo<QWeatherData.GridNow.ReturnBody>()
    )

    object GridDaily : QWeatherEndpoints(
        route = "/v7/grid-weather/{days}",
        returnBodyTypeInfo = typeInfo<QWeatherData.GridDaily.ReturnBody>()
    )

    object GridHourly : QWeatherEndpoints(
        route = "/v7/grid-weather/{hours}",
        returnBodyTypeInfo = typeInfo<QWeatherData.GridHourly.ReturnBody>()
    )


    object PrecipitationMinutely : QWeatherEndpoints(
        route = "/v7/minutely/5m",
        returnBodyTypeInfo = typeInfo<QWeatherData.PrecipitationMinutely.ReturnBody>()
    )

    object Warning : QWeatherEndpoints(
        route = "/v7/warning/now",
        returnBodyTypeInfo = typeInfo<QWeatherData.Warning.ReturnBody>()
    )

    object WarningList : QWeatherEndpoints(
        route = "/v7/warning/list",
        returnBodyTypeInfo = typeInfo<QWeatherData.WarningList.ReturnBody>()
    )

    object AirQualityCurrently : QWeatherEndpoints(
        route = "/airquality/v1/current/{latitude}/{longitude}",
        returnBodyTypeInfo = typeInfo<QWeatherData.AirQualityCurrently.ReturnBody>()
    )

}