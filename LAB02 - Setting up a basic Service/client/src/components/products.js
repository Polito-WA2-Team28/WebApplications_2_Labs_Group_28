
import { useState } from 'react';
import { Button, Col, Container, Row } from 'react-bootstrap';
import { API } from '../API';

export function GetProducts() {

    const [products, setProducts] = useState([]);

    let getProducts = async () => {
        let products = [];
        await API.getAllProducts()
            .then(data => { products = data; setProducts(products) })
            .catch(error => console.log(error));

        return products;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>GET /API/products/</p></Row>
                    <Row><Button onClick={getProducts}>SEND REQUEST</Button></Row>
                    {products.length > 0 && products.map(product => <Row>{product.name}</Row>)}
                </Col>
            </Row>
        </Container>
    </>
}

export function GetProductById() {

    const [product, setProduct] = useState();
    const [id, setId] = useState(); 

    let getProduct = async () => {
        let product;
        await API.getProductById(id)
            .then(data => { product = data; setProduct(product) })
            .catch(error => console.log(error));
        return product;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>GET /API/products/:id</p></Row>
                    <Row><Button onClick={getProduct}>SEND REQUEST</Button></Row>
                    <Row>{product && <Col>{product.name}</Col>}</Row>
                </Col>
            </Row>
        </Container>
    </>
}