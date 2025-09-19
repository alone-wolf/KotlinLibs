package top.writerpass.qweather.sdk

data class QWeatherConfig(
    val apiKey: String? = null,
    val jwtToken: String? = null,
    val baseUrl: String,
    val timeoutMillis: Long = 5000
) {
    init {
        require(apiKey != null || jwtToken != null) { "apiKey or jwtToken must be set" }
    }
}

class QWeatherConfigDsl {
    var apiKey: String? = null
    var jwtToken: String? = null
    var baseUrl: String = ""
    var timeoutMillis: Long = 5000

    fun toConfig(): QWeatherConfig {
        return QWeatherConfig(apiKey, jwtToken, baseUrl, timeoutMillis)
    }
}
