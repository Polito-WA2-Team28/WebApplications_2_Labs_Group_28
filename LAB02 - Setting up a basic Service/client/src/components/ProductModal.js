import dayjs from "dayjs";
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
                    <th>Serial Number</th>
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
                <tbody>
                {props.products !== undefined &&
                    props.products.map((product,i) =>
                        <ProductRow product={product} key={"product " + i} />
                        )}
                    </tbody>
                </Table>
        </Modal.Body>
    </Modal>
}

function ProductRow(props) {

    const warrantyExpirationDate = dayjs(props.product.warrantyExpirationDate).format('YYYY-MM-DD');
    const insuranceExpirationDate = dayjs(props.product.insuranceExpirationDate).format('YYYY-MM-DD');
    const devicePurchaseDate = dayjs(props.product.devicePurchaseDate).format('YYYY-MM-DD');
    const insurancePurchaseDate = dayjs(props.product.insurancePurchaseDate).format('YYYY-MM-DD');


    return <tr key={props.product.serialNumber}>
        <td>{props.product.serialNumber}</td>
        <td>{props.product.deviceType}</td>
        <td>{props.product.model}</td>
        <td>{devicePurchaseDate}</td>
        <td>{props.product.owner}</td>
        <td>{props.product.warrantyDescription}</td>
        <td>{warrantyExpirationDate}</td>
        <td>{insurancePurchaseDate}</td>
        <td>{insuranceExpirationDate}</td>
    </tr>
}