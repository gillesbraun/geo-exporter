package lu.gbraun.geo_exporter.controllers

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mapRoutes() {
    get("/map/url") {
        val mapStyleUrl = System.getenv("MAP_STYLE_URL")
        val mapTilerUrl = System.getenv("MAPTILER_KEY")?.let {
            "/osm_liberty.json?key=$it"
        }
        call.respondText(
            mapStyleUrl
                ?: mapTilerUrl
                ?: throw IllegalArgumentException("specify either MAP_STYLE_URL or MAPTILER_KEY env variables")
        )
    }
}