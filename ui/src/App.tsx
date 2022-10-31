import {Col, Container, Row} from "reactstrap";
import BaseMap from "./BaseMap";
import SearchWidget, {OsmPoly} from "./SearchWidget";
import {useState} from "react";

function App() {
    const [poly, setPoly] = useState<OsmPoly|null>(null)
  return (
    <Container fluid>
        <Row>
            <Col lg="6">
                <h3>Geo Export</h3>
                <SearchWidget
                    onSelected={setPoly}
                />
            </Col>
            <Col lg="6">
                <BaseMap
                    features={poly != null ? [poly] : []}
                />
            </Col>
        </Row>
    </Container>
  );
}

export default App;
