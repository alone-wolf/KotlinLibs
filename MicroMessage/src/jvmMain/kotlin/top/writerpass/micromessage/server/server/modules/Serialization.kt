package top.writerpass.micromessage.server.server.modules

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import top.writerpass.micromessage.server.core.Singletons

internal fun Application.installSerialization() {
    install(ContentNegotiation) {
        json(Singletons.prettyJson)
    }
}