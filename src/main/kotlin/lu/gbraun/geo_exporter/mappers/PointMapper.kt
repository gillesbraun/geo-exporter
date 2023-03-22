package lu.gbraun.geo_exporter.mappers

import lu.gbraun.geo_exporter.dto.OsmPointDto
import lu.gbraun.geo_exporter.entities.OsmPoint
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(uses = [PostgisToGeojsonMapper::class])
interface PointMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(PointMapper::class.java)
    }

    fun mapOsmPoint(osmPolygon: OsmPoint): OsmPointDto
}