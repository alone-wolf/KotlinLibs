package top.writerpass.micromessage.server.server.archive//package top.writerpass
//
//import io.ktor.server.application.*
//import io.ktor.server.engine.*
//import io.ktor.server.netty.*
//import top.writerpass.micromessage.server.configureMonitoring
//
//fun main() {
//    embeddedServer(
//        factory = Netty,
//        port = 8080,
//        host = "0.0.0.0",
//        module = Application::module
//    ).start(wait = true)
//}
//
//fun Application.module() {
//    configureSockets()
//    configureSecurity()
//    configureHTTP()
//    configureTemplating()
//    configureMonitoring()
//    configureSerialization()
//    configureDatabases()
//    configureFrameworks()
//    configureAdministration()
//    configureRouting()
//}
