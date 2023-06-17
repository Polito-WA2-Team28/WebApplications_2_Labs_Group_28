import { Card, CardGroup } from "react-bootstrap";
import EmptySearch from "./EmptySearch";

export function ProductsTab(props) {

    return <>
        <h1>Products</h1>
        <CardGroup>
            {props.products.length === 0 ?
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
                    <p>{`Serial Number: ${props.product.serialNumber}` }</p>
            </Card.Body>
        </Card>
    </>
}