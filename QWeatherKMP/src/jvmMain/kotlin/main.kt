import kotlinx.coroutines.runBlocking
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.qweather.sdk.QWeatherClient

fun main(): Unit = runBlocking {
    launchIO {
        QWeatherClient {
            baseUrl = "https://k63dn236jg.re.qweatherapi.com"
            apiKey = "ffb900024f4048369483d25a15b9a807"
        }.use { qw ->
            qw.weatherApi.weatherNow(location = "116.48641,39.92149").let { it ->
                println(it)
            }
        }
    }
}