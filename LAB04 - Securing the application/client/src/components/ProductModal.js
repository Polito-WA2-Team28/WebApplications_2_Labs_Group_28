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

    const product = props.product

    const warrantyExpirationDate = product.warrantyExpirationDate
    const insuranceExpirationDate = product.insuranceExpirationDate
    const devicePurchaseDate = product.devicePurchaseDate
    const insurancePurchaseDate = product.insurancePurchaseDate


    return <tr key={product.serialNumber}>
        <td>{product.serialNumber}</td>
        <td>{product.deviceType}</td>
        <td>{product.model}</td>
        <td>{formatDateOrNull(devicePurchaseDate)}</td>
        <td style={{textAlign:"center"}}>{props.product.owner}</td>
        <td>{product.warrantyDescription}</td>
        <td>{formatDateOrNull(warrantyExpirationDate)}</td>
        <td>{formatDateOrNull(insurancePurchaseDate)}</td>
        <td>{formatDateOrNull(insuranceExpirationDate)}</td>
    </tr>
}

function formatDateOrNull(date) {
    if (date == null) 
        return "none";
    return dayjs(date).format('YYYY-MM-DD');
}