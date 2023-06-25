import { Button, Card, CardGroup, Modal, Form, Col, Row } from "react-bootstrap";
import EmptySearch from "./EmptySearch";
import { useContext, useState } from "react";
import "../styles/ProductsTab.css"
import { UserContext } from "../Context";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";
import { faUpRightAndDownLeftFromCenter } from "@fortawesome/free-solid-svg-icons";

export function ProductsTab() {

  const productsPage = useContext(UserContext).products;
  const products = productsPage
  //.content;

  const [show, setShow] = useState(false);

  return <>
    <Button onClick={() => setShow(true)}>Register a new product</Button>
    <RegisterNewProductModal show={show} handleClose={() => setShow(false)}
      handleCreate={() => { console.log("CREATE") }}
    />
    <CardGroup>
      {(products === undefined || products.length === 0) ?
        <EmptySearch /> :
        products.map((product) => <ProductItem key={product.id} product={product} />)
      }
    </CardGroup>
  </ >;
}


function ProductItem(props) {

  const navigate = useNavigate();

  return <>
    <Card key={props.product} className="productCard">
      <Card.Body>
        <Card.Title>
          <Row>
            <Col> {props.product.model}</Col>
            <Col className="text-end">
              <Button onClick={() => navigate(`/product/${props.product.id}`)}>
                <FontAwesomeIcon icon={faUpRightAndDownLeftFromCenter} />
              </Button></Col>
          </Row>

        </Card.Title>
        <p>{`Device type: ${props.product.deviceType}`}</p>
        <p>{`Serial Number: ${props.product.serialNumber}`}</p>
      </Card.Body>
    </Card>
  </>
}

function RegisterNewProductModal(props) {

  const [productId, setProductId] = useState("");
  const [serialNumber, setSerialNumber] = useState("");

  const handleCreate = () => {
    const product = { serialNumber }
    props.handleCreate(product);
    props.handleClose();
  }

  return <Modal show={props.show} onHide={props.handleClose} className="custom-modal">
    <Modal.Header closeButton className="custom-modal-header">
      <Modal.Title>Register a new product</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <Form>
        <Form.Group className="mb-3" controlId="productId">
          <Form.Label>Product ID</Form.Label>
          <Form.Control type="text" placeholder="Enter product ID" value={productId} onChange={ev => setProductId(ev.target.value)} className="custom-form-control" />
        </Form.Group>
        <Form.Group className="mb-3" controlId="serialNumber">
          <Form.Label>Serial Number</Form.Label>
          <Form.Control type="text" placeholder="Enter serial number" value={serialNumber} onChange={ev => setSerialNumber(ev.target.value)} className="custom-form-control" />
        </Form.Group>
      </Form>
    </Modal.Body>
    <Modal.Footer className="custom-modal-footer">
      <Button variant="secondary" onClick={props.handleClose} className="custom-button">Close</Button>
      <Button variant="primary" onClick={handleCreate} className="custom-button">Create</Button>
    </Modal.Footer>
  </Modal>
}
