package lu.gbraun.geo_exporter.controllers

import lu.gbraun.geo_exporter.entities.OsmPolygon
import io.ebean.DB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lu.gbraun.geo_exporter.entities.query.QOsmPolygon
import lu.gbraun.geo_exporter.mappers.RegionMapper

fun Route.polygonRoutes() {
    get("/polygons/search/{search}") {
        val search = call.parameters["search"] ?: ""
        val polygons = findPolygonsByName(search)
        call.respond(polygons.map(RegionMapper.INSTANCE::mapOsmPoly))
    }
    get("/polygons/export/{id}/{format}") {
        val id = requireNotNull(call.parameters["id"]?.toIntOrNull())
        val format = requireNotNull(call.parameters["format"])
        val export = exportPolygon(id, format)
        val fixedName = export.name.replace(' ', '-')
            .replace(Regex("[^a-zA-Z0-9]"), "")
            .let { name -> "export-$name-adminlevel_${export.adminLevel}.$format" }
        call.response.header("Content-Disposition", "attachment; filename=\"$fixedName\"")
        call.respondText(export.export, ContentType.Application.OctetStream)
    }
}

private suspend fun findPolygonsByName(search: String): MutableList<OsmPolygon> {
    return withContext(Dispatchers.IO) {
        val query = QOsmPolygon()
            .or()
            .raw("lower(name) like ?", "%$search%".lowercase())
            .raw("lower(\"name:en\") like ?", "%$search%".lowercase())
            .endOr()
            .orderBy()
            .wayArea.desc()
            .adminLevel.asc()
            .setMaxRows(25)
        val results = query.findList()
        //LoggerFactory.getLogger("").info("query: {}", query.generatedSql)
        results
    }
}


data class ExportedPolygon(
    val export: String,
    val name: String,
    val adminLevel: Int,
)
private suspend fun exportPolygon(id: Int, format: String): ExportedPolygon {
    val select = when (format) {
        "kml" -> "ST_AsKml(way)"
        "geojson" -> "ST_AsGeoJSON(way)"
        else -> throw IllegalArgumentException("cant export $format")
    }
    val row = withContext(Dispatchers.IO) {
        DB.sqlQuery("SELECT $select as export, name, admin_level FROM planet_osm_polygon WHERE id = ? LIMIT 1")
            .setParameter(id)
            .findOneOrEmpty()
            .orElseThrow { IllegalArgumentException("Polygon not found in the database") }
    }

    val export = row.getString("export")
    val fixed = when (format) {
        "kml" -> """
            <?xml version="1.0" encoding="UTF-8"?>
            <kml xmlns="http://earth.google.com/kml/2.1">
            <Document>
              <name>${row.getString("name")}</name>
              <Style id="0">    
              </Style> 
                <Placemark>
                <name>${row.getString("name")}</name>
                $export
                </Placemark>
            </Document>
            </kml>
        """.trimIndent()
        else -> export
    }

    return ExportedPolygon(
        export = fixed,
        name = row.getString("name"),
        adminLevel = row.getInteger("admin_level")
    )
}
