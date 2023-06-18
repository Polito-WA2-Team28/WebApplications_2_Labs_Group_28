import { Button, Card, CardGroup, Modal, Form } from "react-bootstrap";
import EmptySearch from "./EmptySearch";
import { useState } from "react";

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

    return <Modal show={props.show} onHide={props.handleClose}>
        <Modal.Header closeButton>
            <Modal.Title>Create a ticket</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form>
                <Form.Group className="mb-3" >
                    <Form.Label>Product ID</Form.Label>
                    <Form.Control type="text" placeholder="Enter product ID" value={productId} onChange={ev => setProductId(ev.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Device Type</Form.Label>
                    <Form.Control type="text" placeholder="Enter device type" value={deviceType} onChange={ev => setDeviceType(ev.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Model</Form.Label>
                    <Form.Control type="text" placeholder="Enter model" value={model} onChange={ev => setModel(ev.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Serial Number</Form.Label>
                    <Form.Control type="text" placeholder="Enter serial number" value={serialNumber} onChange={ev => setSerialNumber(ev.target.value)} />
                </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={props.handleClose}>Close</Button>
            <Button variant="primary" onClick={handleCreate}>Create</Button>
        </Modal.Footer>
    </Modal>
}