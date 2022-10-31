package lu.gbraun.geo_exporter

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    connect()
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
        watchPaths = listOf("resources")
    )
        .start(wait = true)
}

fun Application.module() {
    configure()
    configureRoutes()
}
