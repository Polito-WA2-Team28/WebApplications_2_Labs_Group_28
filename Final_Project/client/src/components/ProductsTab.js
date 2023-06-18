import { Button, Card, CardGroup, Modal, Form } from "react-bootstrap";
import EmptySearch from "./EmptySearch";
import { useState } from "react";
import "../styles/ProductsTab.css"

export function ProductsTab(props) {

    const [show, setShow] = useState(false);

    return <>
        <h1>Products</h1>
        <Button onClick={() => setShow(true)}>Create a ticket</Button>
        <CreationModal show={show} handleClose={() => setShow(false)}
            handleCreate={() => { console.log("CREATE") }}
        />

        <CardGroup>
            {(props.products === undefined || props.products.length === 0) ?
                <EmptySearch /> :
                props.products.map((product) => <ProductItem key={product.serialNumber} product={product} />)
            }
        </CardGroup>
    </>;
}


function ProductItem(props) {
    return <>
        <Card key={props.product}>
            <Card.Body>
                <Card.Title>
                    {props.product.model}
                </Card.Title>
                <p>{`Device type: ${props.product.deviceType}`}</p>
                <p>{`Serial Number: ${props.product.serialNumber}`}</p>
            </Card.Body>
        </Card>
    </>
}

function CreationModal(props) {

    const [productId, setProductId] = useState("");
    const [deviceType, setDeviceType] = useState("");
    const [model, setModel] = useState("");
    const [serialNumber, setSerialNumber] = useState("");

    const handleCreate = () => {
        const product = { deviceType, model, serialNumber }
        props.handleCreate(product);
        props.handleClose();
    }

    return <Modal show={props.show} onHide={props.handleClose} className="custom-modal">
    <Modal.Header closeButton className="custom-modal-header">
      <Modal.Title>Create a ticket</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <Form>
        <Form.Group className="mb-3" controlId="productId">
          <Form.Label>Product ID</Form.Label>
          <Form.Control type="text" placeholder="Enter product ID" value={productId} onChange={ev => setProductId(ev.target.value)} className="custom-form-control" />
        </Form.Group>
        <Form.Group className="mb-3" controlId="deviceType">
          <Form.Label>Device Type</Form.Label>
          <Form.Control type="text" placeholder="Enter device type" value={deviceType} onChange={ev => setDeviceType(ev.target.value)} className="custom-form-control" />
        </Form.Group>
        <Form.Group className="mb-3" controlId="model">
          <Form.Label>Model</Form.Label>
          <Form.Control type="text" placeholder="Enter model" value={model} onChange={ev => setModel(ev.target.value)} className="custom-form-control" />
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