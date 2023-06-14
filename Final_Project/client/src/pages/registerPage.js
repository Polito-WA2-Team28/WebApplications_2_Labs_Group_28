import { useState } from "react";
import { Col, Container, Form, Row, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import CustomerRegistrationForm from "../model/customerRegistrationForm";

function RegisterPage(props) {

    const navigate  = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [phone, setPhone] = useState("");
    const [birthDate, setBirthDate] = useState("");
    const [username, setUsername] = useState("");

    const handleRegistration = () => {
        try {
            const profile = new CustomerRegistrationForm(name, surname, username, dayjs(), birthDate, email, phone, password);
            props.handleRegistration(profile);
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
                    <h1 style={{textAlign: "center"}}>REGISTER</h1>
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
                  <Form.Group className="mb-3" controlId="formBasicName">
                      <Form.Label>Name</Form.Label>
                      <Form.Control type="name" placeholder="Name" onChange={e => setName(e.target.value)} />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicSurname">
                      <Form.Label>Surname</Form.Label>
                      <Form.Control type="surname" placeholder="Surname" onChange={e => setSurname(e.target.value)} />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicUsername">
                      <Form.Label>Surname</Form.Label>
                      <Form.Control type="username" placeholder="Username" onChange={e => setUsername(e.target.value)} />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicBirthDate">
                      <Form.Label>Birth Date</Form.Label>
                      <Form.Control type="date" placeholder="Birth Date" onChange={e => setBirthDate(e.target.value)} />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPhone">
                      <Form.Label>Phone</Form.Label>
                      <Form.Control type="phone" placeholder="Phone" onChange={e => setPhone(e.target.value)} />
                  </Form.Group>
                
                </Form>
            </Row>
            <Row>
              <Col><Button onClick={() => handleRegistration()}>Submit</Button></Col>
              <Col><Button onClick={() => navigate("/login")}>Login</Button></Col>
              
            </Row>
           
      </Container>
    );
}

export default RegisterPage;