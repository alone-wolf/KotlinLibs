package top.writerpass.micromessage.server.server

import org.h2.tools.Server
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.utils.WithLogger

class DatabaseContainer: WithLogger{
    val database = Database.connect(
        url = "jdbc:h2:file:./db.h2/storage;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    private val webServer by lazy {
        Server.createWebServer(
            "-web",
            "-webAllowOthers",
            "-webPort", "8082"
        )!!
    }
    // http://127.0.0.1:8082

    fun startWebServer() {
        webServer.start()
    }

    override val logger: org.slf4j.Logger = LoggerFactory.getLogger(DatabaseContainer::class.simpleName)
}