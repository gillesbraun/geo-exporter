package lu.gbraun.geo_exporter.controllers

import io.ebean.DB
import io.ebean.RawSqlBuilder
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lu.gbraun.geo_exporter.entities.OsmPoint
import lu.gbraun.geo_exporter.mappers.PointMapper

fun Route.stopRoutes() {
    get("/stops/bus_stops/{region}") {
        val region = requireNotNull(call.parameters["region"]?.toIntOrNull())
        val stops = getBusStopsInRegion(region)
        val dtos = stops.map(PointMapper.INSTANCE::mapOsmPoint)
        call.respond(dtos)
    }
}

private suspend fun getBusStopsInRegion(regionId: Int) = withContext(Dispatchers.IO) {
    val rawSql = RawSqlBuilder.parse(
        """
            SELECT min(osm_id) id, name, st_centroid(st_union(way)) way FROM planet_osm_point
            WHERE 
                name IS NOT NULL
                AND
                highway = 'bus_stop'
                AND
                ST_WITHIN(way, (SELECT t1.way FROM planet_osm_polygon t1 WHERE t1.id = :region))
            GROUP BY name
        """.trimIndent()
    )
        .columnMapping("name", "name")
        .columnMapping("way", "way")
        .columnMapping("id", "id")
        .create()

    DB.find(OsmPoint::class.java)
        .setRawSql(rawSql)
        .setParameter("region", regionId)
        .findList()
}