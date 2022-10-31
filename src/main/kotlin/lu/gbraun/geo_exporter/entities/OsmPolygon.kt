package lu.gbraun.geo_exporter.entities

import io.ebean.Model
import org.postgis.Polygon
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "planet_osm_polygon")
class OsmPolygon : Model() {
    @Id
    @Column(name = "osm_id")
    var id: Int? = null
    var name: String? = null
    var adminLevel: Int? = null
    var way: Polygon? = null
    var boundary: String? = null

/*
    var access: String? = null
    @Column(name = "addr:housename")
    var addrHousename: String? = null

    @Column(name = "addr:housenumber")
    var addrHousenumber: String? = null

    @Column(name = "addr:interpolation")
    var addrInterpolation: String? = null


    var aerialway: String? = null
    var aeroway: String? = null
    var amenity: String? = null
    var area: String? = null
    var barrier: String? = null
    var bicycle: String? = null
    var brand: String? = null
    var bridge: String? = null
    var building: String? = null
    var construction: String? = null
    var covered: String? = null
    var culvert: String? = null
    var cutting: String? = null
    var denomination: String? = null
    var disused: String? = null
    var embankment: String? = null
    var foot: String? = null

    @Column(name = "generator:source")
    var generatorSource: String? = null

    var harbour: String? = null
    var highway: String? = null
    var historic: String? = null
    var horse: String? = null
    var intermittent: String? = null
    var junction: String? = null
    var landuse: String? = null
    var layer: String? = null
    var leisure: String? = null
    var lock: String? = null
    var manMade: String? = null
    var military: String? = null
    var motorcar: String? = null
    var natural: String? = null
    var office: String? = null
    var oneway: String? = null
    var operator: String? = null
    var place: String? = null
    var population: String? = null
    var power: String? = null
    var powerSource: String? = null
    var publicTransport: String? = null
    var railway: String? = null
    var ref: String? = null
    var religion: String? = null
    var route: String? = null
    var service: String? = null
    var shop: String? = null
    var sport: String? = null
    var surface: String? = null
    var toll: String? = null
    var tourism: String? = null

    @Column(name = "tower:type")
    var towerType: String? = null


    var tracktype: String? = null
    var tunnel: String? = null
    var water: String? = null
    var waterway: String? = null
    var wetland: String? = null
    var width: String? = null
    var wood: String? = null
    var zOrder: Int? = null
    var wayArea: Int? = null
 */
}