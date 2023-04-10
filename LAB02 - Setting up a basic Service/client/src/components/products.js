
import { useState } from 'react';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';
import { API } from '../API';
import { ProductModal } from './ProductModal';
import { errorHandler } from './ErrorHandler';

export function GetProducts() {

    const [products, setProducts] = useState([]);
    const [error, setError] = useState();
    const [show, setShow] = useState(false);

    let getProducts = async () => {
        let products = [];
        await API.getAllProducts()
            .then(data => { products = data; setProducts(products); console.log(products); setShow(true) })
            .catch(error => {setError(error); errorHandler(error)});

        return products;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row ><p style={{ textAlign: 'center' }}>GET /API/products/</p></Row>
                    <Row><Button onClick={getProducts}>SEND REQUEST</Button></Row>
                    {products.length > 0 && products.map(product => <Row>{product.name}</Row>)}
                    {<ProductModal show={show} products={products} handleClose={()=>setShow(false)} />}
                </Col>
            </Row>
        </Container>
    </>
}

export function GetProductById(props) {

    const [product, setProduct] = useState();
    const [id, setId] = useState(); 
    const [error, setError] = useState();
    const [show, setShow] = useState(false);

    let getProduct = async () => {
        let product;
        await API.getProductById(id)
            .then(data => { product = data; setProduct(product);  setShow(true)})
            .catch(error => {setError(error); errorHandler(error)});
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
                    {<ProductModal show={show} products={[product]} handleClose={()=>setShow(false)} />}

                </Col>
            </Row>
        </Container>
    </>
}