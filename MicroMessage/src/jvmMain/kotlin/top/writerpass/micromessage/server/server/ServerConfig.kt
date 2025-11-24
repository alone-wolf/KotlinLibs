package top.writerpass.micromessage.server.server

data class ServerConfig(
    val host: String,
    val port: Int,
    val lifecycleHook: ServerLifecycleMonitor.Configuration
) {
    companion object {
        val default = ServerConfig(
            host = "0.0.0.0",
            port = 8080,
            lifecycleHook = ServerLifecycleMonitor.Configuration().apply {
                onStarting = {
                    println("Application is starting...")
                }
                onStarted = {
                    println("Application started successfully.")
                }
                onStopping = {
                    println("Application is stopping, cleaning up...")
                }
                onStopped = {
                    println("Application fully stopped.")
                }
            }
        )
    }
}

