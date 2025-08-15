import kotlinx.coroutines.runBlocking
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.qweather.QWeatherClient
import java.io.File

fun main(): Unit = runBlocking {
    launchIO {
        QWeatherClient(
            apiHost = "k63dn236jg.re.qweatherapi.com",
            apiKey = "ffb900024f4048369483d25a15b9a807"
        ).use {qw->
            val r = qw.getNowWeather("116.48641,39.92149")
            println(r.toString())
        }
    }
}