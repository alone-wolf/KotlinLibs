package top.writerpass.qweather.ktor

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO

actual val clientEngine: HttpClientEngineFactory<*> = CIO