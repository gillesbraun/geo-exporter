package lu.gbraun.geo_exporter.dto

import org.geojson.Point

data class OsmPointDto(
    var id: Long? = null,
    var name: String? = null,
    var way: Point? = null,
)