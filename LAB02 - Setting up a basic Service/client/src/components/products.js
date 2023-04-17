
import { useState } from 'react';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';
import { API } from '../API';
import { ProductModal } from './ProductModal';
import { errorHandler } from './ErrorHandler';

export function GetProducts() {

    const [products, setProducts] = useState([]);
    const [show, setShow] = useState(false);

    let getProducts = async () => {
        let products = [];
        await API.getAllProducts()
            .then(data => { products = data; setProducts(products);setShow(true) })
            .catch(error => errorHandler(error));
    }

    return <>
        <Container>
            <Row>
            <Col xl={3}/>

                <Col>
                    <Row ><p style={{ textAlign: 'center' }}>GET ALL PRODUCTS</p></Row>
                    <Row><Button onClick={getProducts}>SEND REQUEST</Button></Row>
                    {<ProductModal show={show} products={products} handleClose={()=>setShow(false)} />}
                </Col>
            <Col xl={3}/>

            </Row>
        </Container>
    </>
}

export function GetProductById(props) {

    const [product, setProduct] = useState(null);
    const [id, setId] = useState(""); 
    const [show, setShow] = useState(false);

    let getProduct = async () => {

        const reID = RegExp('^[0-9]+$');
        if(!reID.test(id)) return errorHandler("ID is not valid");

        await API.getProductById(id)
            .then(data => { setProduct(data);  setShow(true)})
            .catch(error =>  errorHandler(error));
    }

    return <>
        <Container>
            <Row>
            <Col xl={3}/>

                <Col>
                    <Row><p style={{ textAlign: 'center' }}>FIND A PRODUCT BY ID</p></Row>
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
            <Col xl={3}/>

            </Row>
        </Container>
    </>
}