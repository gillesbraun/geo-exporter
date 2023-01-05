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

## prepare osm data with osm2pgsql

### install requirements

```shell
sudo apt-get install -y postgis postgresql make unzip cmake g++ \ 
  libboost-dev libboost-system-dev libboost-filesystem-dev libexpat1-dev \ 
  zlib1g-dev libbz2-dev libpq-dev libproj-dev lua5.3 liblua5.3-dev pandoc \ 
  libluajit-5.1-dev osmium-tool
```
### compile osm2pgsql

```shell
wget https://github.com/openstreetmap/osm2pgsql/archive/refs/tags/1.7.2.zip
unzip 1.7.2.zip && rm 1.7.2.zip
cd osm2pgsql-1.7.2
mkdir build && cd build
cmake -D WITH_LUAJIT=ON ..
make
make man
sudo make install
cd
```

#### configure postgres

```shell
cat <<EOT | sudo tee -a /etc/postgresql/14/main/postgresql.conf
shared_buffers=10GB
work_mem=50MB
maintenance_work_mem=10GB
autovacuum_work_mem=2GB
wal_level=minimal
checkpoint_timeout=60min
max_wal_size=10GB
checkpoint_completion_target=0.9
max_wal_senders=0
random_page_cost=1.0
EOT

echo "127.0.0.1:5432:osm:user:password" > .pgpass

sudo service postgresql restart

sudo su - postgres
psql -c "create role username with password 'password';"
psql -c "alter role username with login;"
psql -c "create database osm;"
psql -c "grant all on database osm to username;"
psql -c "create extension postgis;" osm
exit
```

### Filter planet down to only areas with admin_level

```shell
time osmium tags-filter -o planet_areas_test.osm.pbf planet-latest.osm.pbf a/admin_level
```

### Custom style for importing into osm2pgsql

added coastline and `name:en`

```shell
cp /usr/share/osm2pgsql/default.style custom.style
echo "node,way   name:en      text         linear" >> custom.style
```

### run osm2pgsql (in screen or tmux)

```shell
time osm2pgsql --cache 150000 -d osm -U username -H 127.0.0.1 \
--keep-coastlines --latlong --style=custom.style \
planet_areas.osm.pbf
```

### create indices on finished data

Also adds an ID column because osm_id are not unique, 

```shell
psql -h 127.0.0.1 -U username -c "CREATE INDEX planet_osm_polygon_admin_level_idx on planet_osm_polygon USING btree (admin_level)" osm &
psql -h 127.0.0.1 -U username -c "CREATE INDEX planet_osm_polygon_boundary_idx on planet_osm_polygon USING btree (boundary)" osm &
psql -h 127.0.0.1 -U username -c "CREATE INDEX planet_osm_polygon_name_lower_idx on planet_osm_polygon USING btree (lower(name) varchar_pattern_ops)" osm &
psql -h 127.0.0.1 -U username -c "CREATE INDEX planet_osm_polygon_name_en_lower_idx on planet_osm_polygon USING btree (lower(\"name:en\") varchar_pattern_ops)" osm &
psql -h 127.0.0.1 -U username -c "CREATE INDEX planet_osm_polygon_osm_id_idx on planet_osm_polygon USING btree (osm_id)" osm &
psql -h 127.0.0.1 -U username -c "ALTER TABLE planet_osm_polygon ADD COLUMN id SERIAL PRIMARY KEY;" osm \
  && psql -h 127.0.0.1 -U username -c "CREATE UNIQUE INDEX planet_osm_polygon_id_unique_idx on planet_osm_polygon USING btree (id)" osm &
```
