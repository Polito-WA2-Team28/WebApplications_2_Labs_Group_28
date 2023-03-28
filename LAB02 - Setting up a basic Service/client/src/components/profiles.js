import { useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { API } from "../API";

export function GetProfileByEmailAddress() {

    const [profile, setProfile] = useState([]);
    const [error, setError] = useState();

    let getProfile = async () => {
        let products = [];
        await API.getProfileByEmailAddress()
            .then(data => { products = data; setProfile(products) })
            .catch(error => setError("Error: " + error));

        return products;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>GET /API/profiles/:emailAddress</p></Row>
                    <Row><Button onClick={getProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                    {error && <Row><Col>{error}</Col></Row>}

                </Col>
            </Row>
        </Container>
    </>
}

export function CreateProfile() {

    const [profile, setProfile] = useState();
    const [email, setEmail] = useState();
    const [name, setName] = useState();
    const [surname, setSurname] = useState();
    const [error, setError] = useState();

    let createProfile = async () => {
        let profile = { email: email, name: name, surname:surname}
        await API.createProfile(profile)
            .then(data => { profile = data; setProfile(profile)})
            .catch(error => setError("Error: " + error));
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>POST /API/profiles/:emailAddress</p></Row>
                    <Row>
                        <Form>
                            <Form.Group className="mb-3" controlId="email">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="email" placeholder="Enter email" onChange={e => setEmail(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="name">
                                <Form.Label>Name</Form.Label>
                                <Form.Control type="name" placeholder="Enter name" onChange={e => setName(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="surname">
                                <Form.Label>Surname</Form.Label>
                                <Form.Control type="surname" placeholder="Enter surname" onChange={e => setSurname(e.target.value)} />
                            </Form.Group>
                        </Form>
                    </Row>
                    <Row><Button onClick={createProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                    {error && <Row><Col>{error}</Col></Row>}

                </Col>
            </Row>
        </Container>
    </>
}

export function UpdateProfile() {

    const [profile, setProfile] = useState();
    const [email, setEmail] = useState();
    const [name, setName] = useState();
    const [surname, setSurname] = useState();
    const [error, setError] = useState();

    let updateProfile = async () => {
        let profile = { email: email, name: name, surname:surname}
        await API.updateProfile(profile)
            .then(data => { profile = data; setProfile(profile)})
            .catch(error => setError("Error: " + error));
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>PUT /API/profiles/:emailAddress</p></Row>
                    <Form>
                            <Form.Group className="mb-3" controlId="email">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="email" placeholder="Enter email" onChange={e => setEmail(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="name">
                                <Form.Label>Name</Form.Label>
                                <Form.Control type="name" placeholder="Enter name" onChange={e => setName(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="surname">
                                <Form.Label>Surname</Form.Label>
                                <Form.Control type="surname" placeholder="Enter surname" onChange={e => setSurname(e.target.value)} />
                            </Form.Group>
                        </Form>
                    <Row><Button onClick={updateProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                    {error && <Row><Col>{error}</Col></Row>}

                </Col>
            </Row>
        </Container>
    </>
}