package lu.gbraun.geo_exporter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.event.Level

fun Application.configure() {

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            configure(JsonGenerator.Feature.IGNORE_UNKNOWN, false)
        }
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest, mapOf(
                    "error" to "ILLEGAL_ARGUMENT",
                    "message" to cause.message
                )
            )
        }
        exception<Exception> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError, mapOf(
                    "error" to "INTERNAL_SERVER_ERROR",
                    "message" to "An unexpected internal error occurred."
                )
            )
        }
    }

    configureRoutes()
}

