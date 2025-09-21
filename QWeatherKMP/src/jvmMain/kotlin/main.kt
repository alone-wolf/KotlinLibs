import kotlinx.coroutines.runBlocking
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.kmplibrary.coroutine.withContextIO
import top.writerpass.kmplibrary.utils.println
import top.writerpass.qweather.sdk.QWeatherClient
import top.writerpass.qweather.sdk.QWeatherErrorResponseException
import java.io.File

public suspend fun loadConfig(): Map<String, String> {
    return withContextIO {
        val configMap = mutableMapOf<String, String>()
        File("./qweather.conf").readLines().forEach { line ->
            val r = line.split("=")
            configMap[r.first()] = r.last()
        }
        configMap as Map<String, String>
    }
}

class QWeatherRepository(
    private val client: QWeatherClient
) {

    fun onClose() {
        client.close()
    }

    suspend fun <T> request(
        block: suspend QWeatherClient.() -> T,
        onSuccess: suspend (T) -> Unit,
        onQWeatherErrorResponse: suspend (QWeatherErrorResponseException) -> Unit = { println(it.error.detail) },
        onThrowable: suspend (Throwable) -> Unit = { println(it.localizedMessage) }
    ) {
        runCatching {
            onSuccess(client.block())
        }.also {
            it.exceptionOrNull()?.let { exception ->
                if (exception is QWeatherErrorResponseException) {
                    onQWeatherErrorResponse(exception)
                } else {
                    onThrowable(exception)
                }
            }
        }
    }

    suspend fun getNowWeather(location: String) {
        return request(
            block = { client.weatherApi.weatherNow(location) },
            onSuccess = {},
        )
    }

}

fun main(): Unit = runBlocking {
    launchIO {
        val config = loadConfig()
        val client = QWeatherClient {
            baseUrl = config["baseUrl"]!!
            apiKey = config["apiKey"]!!
        }

        QWeatherClient {
            baseUrl = config["baseUrl"]!!
            apiKey = config["apiKey"]!!
        }.use { qw ->
            runCatching {
                qw.weatherApi.weatherNow(location = "116.48641,39.92149").let { it ->
//                    val icon = it.now.icon
//                    QWeatherIcons.icon(icon).decodeToString().println()
                }
            }.also {
                it.exceptionOrNull()?.let { exception ->
                    if (exception is QWeatherErrorResponseException) {
                        println(exception.localizedMessage)
                    }
                }
            }
        }
    }
}