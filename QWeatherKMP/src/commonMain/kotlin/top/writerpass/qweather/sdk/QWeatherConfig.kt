package top.writerpass.qweather.sdk

data class QWeatherConfig(
    val apiKey: String? = null,
    val jwtToken: String? = null,
    val baseUrl: String = "https://devapi.qweather.com",
    val timeoutMillis: Long = 5000
) {
    init {
        require(apiKey != null || jwtToken != null) { "apiKey or jwtToken must be set" }
    }
}
