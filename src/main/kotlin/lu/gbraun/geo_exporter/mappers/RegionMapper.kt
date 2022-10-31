package lu.gbraun.geo_exporter.mappers

import lu.gbraun.geo_exporter.dto.OsmPolygonDto
import lu.gbraun.geo_exporter.entities.OsmPolygon
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(uses = [PostgisToGeojsonMapper::class])
interface RegionMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(RegionMapper::class.java)
    }

    fun mapOsmPoly(osmPolygon: OsmPolygon): OsmPolygonDto
}