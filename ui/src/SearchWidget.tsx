import {Button, Form, FormGroup, Input, ListGroup, ListGroupItem} from "reactstrap";
import {useState} from "react";
import {axios} from "./index";
import {Polygon} from "ol/geom";

export interface SearchWidgetProps {
    onSelected: (poly: OsmPoly | null) => void
}

export interface OsmPoly {
    name: string
    id: number
    way: Polygon
    adminLevel: number
}

const SearchWidget = (props: SearchWidgetProps) => {
    const [loading, setLoading] = useState(false)
    const [search, setSearch] = useState("")
    const [searchString, setSearchString] = useState("")
    const [items, setItems] = useState<OsmPoly[]>([])

    const doSearch = () => {
        setLoading(true)
        setItems([])
        const searched = search
        axios.get("/polygons/search/" + searched)
            .then(res => {
                const data = JSON.parse(res.data) as OsmPoly[]
                setSearchString(searched)
                setItems(data)
            })
            .catch(console.error)
            .finally(() => {
                setLoading(false)
            })
    }

    const download = (id: number, format: string) => {
        document.location.href = document.location.host + `/polygons/export/${id}/${format}`
    }

    function select(poly: OsmPoly) {
        props.onSelected(poly)
    }

    return <>
        <Form onSubmit={e => {
            e.preventDefault()
            doSearch()
        }}>
            <FormGroup>
                <Input
                    autoFocus
                    type={"text"}
                    placeholder={"Search Cities, Towns, Countries..."}
                    value={search}
                    onChange={e => {
                        setSearch(e.target.value)
                    }}
                />
            </FormGroup>
            <FormGroup>
                <Button
                    disabled={search.length < 3 || loading}
                    color={"primary"}
                >
                    Search
                </Button>
            </FormGroup>
        </Form>

        <hr/>

        <h4>Search results for: {searchString}</h4>

        <ListGroup>
            {items.map(poly => (
                <ListGroupItem
                    key={"poly-" + poly.id + "-" + poly.adminLevel}
                >
                    <a onClick={e => select(poly)} style={{cursor: "pointer"}}>
                        {poly.name}, adminLevel: {poly.adminLevel}
                    </a>
                    <div className="float-end">
                        <a href={`/polygons/export/${poly.id}/kml`} className={"btn btn-info btn-sm text-white me-1"}>
                            KML
                        </a>
                        <a href={`/polygons/export/${poly.id}/geojson`} className={"btn btn-info btn-sm text-white"}>
                            GeoJSON
                        </a>
                    </div>
                </ListGroupItem>
            ))}
        </ListGroup>
    </>
}

export default SearchWidget