package lu.gbraun.geo_exporter.mappers

import org.geojson.LngLatAlt
import org.postgis.Point
import org.postgis.Polygon

@Suppress("MemberVisibilityCanBePrivate") // used by mappers
class PostgisToGeojsonMapper {
    companion object {
        @JvmStatic
        val INSTANCE = PostgisToGeojsonMapper()
    }

    fun mapPolygonToGeoJson(polygon: Polygon?): org.geojson.Polygon? {
        if (polygon == null) {
            return null
        }
        val polygonJson = org.geojson.Polygon()
        val coordinates: MutableList<List<LngLatAlt>> = ArrayList()
        for (i in 0 until polygon.numRings()) {
            val coordinatesSubList: MutableList<LngLatAlt> = ArrayList()
            val ring = polygon.getRing(i)
            for (point in ring.points) {
                coordinatesSubList.add(mapPostgisPointToLngLatAlt(point))
            }
            coordinates.add(coordinatesSubList)
        }
        polygonJson.coordinates = coordinates
        return polygonJson
    }

    fun mapPostGisPointToGeoJson(point: Point): org.geojson.Point {
        return if (point.getZ() > 0) {
            org.geojson.Point(point.getX(), point.getY(), point.getZ())
        } else {
            org.geojson.Point(point.getX(), point.getY())
        }
    }

    fun mapPostgisPointToLngLatAlt(point: Point): LngLatAlt {
        return if (point.getZ() > 0) {
            LngLatAlt(point.getX(), point.getY(), point.getZ())
        } else {
            LngLatAlt(point.getX(), point.getY())
        }
    }

}