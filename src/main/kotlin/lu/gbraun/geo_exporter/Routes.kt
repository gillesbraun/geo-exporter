package lu.gbraun.geo_exporter

import lu.gbraun.geo_exporter.controllers.mapRoutes
import lu.gbraun.geo_exporter.controllers.polygonRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import lu.gbraun.geo_exporter.controllers.pointRoutes


fun Application.configureRoutes() {
    routing {
        get("/") {
            call.respondRedirect("/index.html")
        }
        polygonRoutes()
        pointRoutes()
        mapRoutes()
        static("/") {
            resources("static")
        }
    }
}