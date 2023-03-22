package lu.gbraun.geo_exporter.entities

import org.postgis.Point
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "planet_osm_point")
class OsmPoint {
    @Column(name = "osm_id")
    var id: Long? = null
    var name: String? = null
    var way: Point? = null
}