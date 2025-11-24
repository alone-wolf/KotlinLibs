package top.writerpass.micromessage.server.server

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.util.AttributeKey

/**
 * 一个用于监控 Ktor Server 生命周期状态的插件。
 * 允许在不同阶段注册回调，比如启动、停止等。
 */
class ServerLifecycleMonitor(config: Configuration) {

    private val onStarting = config.onStarting
    private val onStarted = config.onStarted
    private val onStopping = config.onStopping
    private val onStopped = config.onStopped

    fun register(pipeline: Application) {
        pipeline.monitor.subscribe(ApplicationStarting) { app ->
            onStarting?.invoke(app)
        }
        pipeline.monitor.subscribe(ApplicationStarted) { app ->
            onStarted?.invoke(app)
        }
        pipeline.monitor.subscribe(ApplicationStopping) { app ->
            onStopping?.invoke(app)
        }
        pipeline.monitor.subscribe(ApplicationStopped) { app ->
            onStopped?.invoke(app)
        }
    }

    class Configuration {
        var onStarting: ((Application) -> Unit)? = null
        var onStarted: ((Application) -> Unit)? = null
        var onStopping: ((Application) -> Unit)? = null
        var onStopped: ((Application) -> Unit)? = null
    }

    companion object Plugin : BaseApplicationPlugin<Application, Configuration, ServerLifecycleMonitor> {
        override val key = AttributeKey<ServerLifecycleMonitor>("ServerLifecycleMonitor")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): ServerLifecycleMonitor {
            val config = Configuration().apply(configure)
            val monitor = ServerLifecycleMonitor(config)
            monitor.register(pipeline)
            return monitor
        }
    }
}