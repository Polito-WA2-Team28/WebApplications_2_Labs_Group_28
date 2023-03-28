
import { useState } from 'react';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';
import { API } from '../API';

export function GetProducts() {

    const [products, setProducts] = useState([]);
    const [error, setError] = useState();

    let getProducts = async () => {
        let products = [];
        await API.getAllProducts()
            .then(data => { products = data; setProducts(products) })
            .catch(error => setError("Error: " + error));

        return products;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row ><p style={{ textAlign: 'center' }}>GET /API/products/</p></Row>
                    <Row><Button onClick={getProducts}>SEND REQUEST</Button></Row>
                    {products.length > 0 && products.map(product => <Row>{product.name}</Row>)}
                    {error && <Row><Col>{error}</Col></Row>}
                </Col>
            </Row>
        </Container>
    </>
}

export function GetProductById() {

    const [product, setProduct] = useState();
    const [id, setId] = useState(); 
    const [error, setError] = useState();

    let getProduct = async () => {
        let product;
        await API.getProductById(id)
            .then(data => { product = data; setProduct(product) })
            .catch(error => setError("Error: " + error));
        return product;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>GET /API/products/:id</p></Row>
                    <Row>
                        <Form>
                            <Form.Group className="mb-3" controlId="productID">
                                <Form.Label>Product ID</Form.Label>
                                <Form.Control type="text" placeholder="Enter product ID" onChange={e => setId(e.target.value)} />
                            </Form.Group>
                        </Form>
                    </Row>
                    <Row><Button onClick={getProduct}>SEND REQUEST</Button></Row>
                    <Row>{product && <Col>{product.name}</Col>}</Row>
                    {error && <Row><Col>{error}</Col></Row>}
                </Col>
            </Row>
        </Container>
    </>
}