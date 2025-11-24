package top.writerpass.micromessage.server.server.modules

import io.ktor.server.application.Application
import io.ktor.server.application.install
import top.writerpass.micromessage.server.server.ServerLifecycleMonitor

fun Application.installLifecycleHook(config: ServerLifecycleMonitor.Configuration) {
    install(ServerLifecycleMonitor) {
        onStarting = config.onStarting
        onStarted = config.onStarted
        onStopping = config.onStopping
        onStopped = config.onStopped
    }
}