import { Modal, Table } from "react-bootstrap";

 

export function ProductModal(props) {
    //return a modal
    return <Modal fullscreen show={props.show} onHide={props.handleClose}>
        <Modal.Header closeButton>
            <Modal.Title>Products</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Table>
                <thead>
                    <tr>
                    <th>UUID</th>
                    <th>deviceType</th>
                    <th>model</th>
                    <th>devicePurchaseDate</th>
                    <th>owner</th>
                    <th>warrantyDescription</th>
                    <th>warrantyExpirationDate</th>
                    <th>insurancePurchaseDate</th>
                    <th>insuranceExpirationDate</th>
                    </tr>
                </thead>
                {props.products !== undefined &&
                    props.products.map((product,i) =>
                        <ProductRow product={product} key={"product " + i} />
                    )}
                </Table>
        </Modal.Body>
    </Modal>
}

function ProductRow(props) {
    return <tr key={props.UUID}>
        <td>{props.UUID}</td>
        <td>{props.deviceType}</td>
        <td>{props.model}</td>
        <td>{props.devicePurchaseDate}</td>
        <td>{props.owner}</td>
        <td>{props.warrantyDescription}</td>
        <td>{props.warrantyExpirationDate}</td>
        <td>{props.insurancePurchaseDate}</td>
        <td>{props.insuranceExpirationDate}</td>
    </tr>
}