package lu.gbraun.geo_exporter.dto

import org.geojson.Polygon

data class OsmPolygonDto(
    var id: Long? = null,
    var name: String? = null,
    var adminLevel: Int? = null,
    var way: Polygon? = null,
    var boundary: String? = null,
)