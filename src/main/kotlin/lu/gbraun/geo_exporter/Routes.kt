package lu.gbraun.geo_exporter

import lu.gbraun.geo_exporter.controllers.mapRoutes
import lu.gbraun.geo_exporter.controllers.polygonRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRoutes() {
    routing {
        get("/") {
            call.respondRedirect("/index.html")
        }
        polygonRoutes()
        mapRoutes()
        static("/") {
            resources("static")
        }
    }
}