# geo-exporter

small tool to export polygons from openstreetmaps as kml or GeoJSON.
Uses a PostGIS DB as storage for searching and keeping all OSM data.

## deploy

use the docker image: `ghcr.io/gillesbraun/geo-exporter/geo-exporter`

You need a source for base map tiles.
You can either use your own mapboxGL style by specifying `MAP_STYLE_URL` 
or register on [maptiler](https://cloud.maptiler.com/account/keys/) and use
the `MAPTILER_KEY` env var, which uses the osm_liberty bright style.

And you need a PostGIS DB already filled with osm data. Use osm2pgsql for this.

```
docker run --rm -it \
  -p 8080:8080 \
  -e DB_HOST=localhost \
  -e DB_USER=user \
  -e DB_PASSWORD=password \
  -e DB_DATABASE=data \
  -e MAPTILER_KEY=key \
  -e MAP_STYLE_URL="https://use-your-own-mapbox-gl-style/style.json" \
  ghcr.io/gillesbraun/geo-exporter/geo-exporter:latest

```