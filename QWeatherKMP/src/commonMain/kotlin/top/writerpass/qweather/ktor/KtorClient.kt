package top.writerpass.qweather.ktor

import io.ktor.client.engine.HttpClientEngineFactory

expect val clientEngine: HttpClientEngineFactory<*>

//fun <T : HttpClientEngineConfig> createHttpClient(
//    engineFactory: HttpClientEngineFactory<T>,
//    block: HttpClientConfig<T>.() -> Unit
//): HttpClient {
//    return HttpClient(engineFactory) {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//            })
//        }
//        install(ContentEncoding) {
//            deflate(1.0F)
//            gzip(0.9F)
//            identity()
//        }
//        block()
//    }
//}