import * as proj from "ol/proj";
import {useEffect, useRef, useState} from "react";
import {OsmPoly} from "./SearchWidget";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {Fill, Stroke, Style} from "ol/style";
import {GeoJSON} from "ol/format";
import * as extent from "ol/extent";
import {apply} from "ol-mapbox-style";
import {Map, View} from "ol";
import {axios} from "./index";

export interface BaseMapProps {
    features: OsmPoly[]
}

const BaseMap = (props: BaseMapProps) => {
    const mapRef = useRef<HTMLDivElement>() as React.MutableRefObject<HTMLInputElement>
    const [map, setMap] = useState<Map>()

    useEffect(() => {

        map?.getLayers().forEach(layer => {
            if (layer instanceof VectorLayer) {
                map?.removeLayer(layer)
            }
        })
        const features = props.features.map(f => new GeoJSON({
            featureProjection: "EPSG:3857",
            dataProjection: "EPSG:4326",
        }).readFeature(f.way))

        const selectedExtent = extent.boundingExtent([])
        features.forEach(f => {
            let extent1 = f?.getGeometry()?.getExtent()
            extent1 && extent.extend(selectedExtent, extent1)
        })
        map?.getView().fit(selectedExtent, {duration: 300, padding: [100, 100, 100, 100]})
        map?.addLayer(
            new VectorLayer({
                source: new VectorSource({
                    features: features
                }),
                style: new Style({
                    stroke: new Stroke({
                        color: 'blue',
                        width: 3,
                    }),
                    fill: new Fill({
                        color: 'rgba(0, 0, 255, 0.1)',
                    }),
                })
            })
        )
        console.log("adding layers")
        console.log(features)
    }, [props.features])

    useEffect(() => {

        const initialMap = new Map({
            target: mapRef.current,
            view: new View({
                center: proj.transform([6, 49.7], "EPSG:4326", "EPSG:3857"),
                zoom: 10,
            }),
            layers: [],
        });
        axios.get("/map/url")
            .then(r =>apply(initialMap, r.data))
            .then(() => {
                setMap(initialMap)
            })
            .catch(e => {
                console.error(e)
                alert("error creating map: " + e)
            })
    }, [])

    return <div ref={mapRef} style={{height: "100vh"}}/>
}

export default BaseMap;