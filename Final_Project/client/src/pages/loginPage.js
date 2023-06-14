import { useState } from "react";
import { Col, Container, Form, Row, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

function LoginPage(props) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate  = useNavigate();

    const handleLogin = async () => {
        try {
            await props.handleLogin(email, password);
            navigate("/dashboard");
        }
        catch (error) {
            console.error(error);
        }
    }


    return (
        <Container fluid >
            <Row>
                <Col>
                    <h1 style={{textAlign: "center"}}>LOGIN</h1>
                  </Col>
            </Row>
            <Row style={{
                marginTop: "5%", width: "60%", marginRight: "20%", marginLeft: "20%",
                display: "flex", justifyContent: "center",
                alignItems: "center"
            }}>
                <Form>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control type="email" placeholder="Enter email" onChange={e =>  setEmail(e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formBasicPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type="password" placeholder="Password" onChange={e =>  setPassword(e.target.value)}/>
                        <Form.Text className="text-muted"/>
                    </Form.Group>
                </Form>
            </Row>
            <Row>
                <Col><Button onClick={handleLogin}>Submit</Button></Col>
                <Col><Button onClick={() => navigate("/register")}>Register</Button></Col>
            </Row>
      </Container>
    );
}

export default LoginPage