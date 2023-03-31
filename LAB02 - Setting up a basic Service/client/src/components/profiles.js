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
            .catch(error => setError(error));

        return products;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>GET /API/profiles/:emailAddress</p></Row>
                    <Row><Button onClick={getProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                    {error && <Row><Col>{String(error)}</Col></Row>}

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
    const [birthdate, setBirtdate] = useState();
    const [phoneNumber, setPhoneNumber] = useState();
    const [error, setError] = useState();

    let createProfile = async () => {
        let profile = { email: email, name: name, surname:surname, birthdate:birthdate, phoneNumber:phoneNumber}
        await API.createProfile(profile)
            .then(data => { profile = data; setProfile(profile)})
            .catch(error => setError(error));
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
                                <Form.Control value={email} placeholder="Enter email" onChange={e => setEmail(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="name">
                                <Form.Label>Name</Form.Label>
                                <Form.Control value={name} placeholder="Enter name" onChange={e => setName(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="surname">
                                <Form.Label>Surname</Form.Label>
                                <Form.Control value={surname} placeholder="Enter surname" onChange={e => setSurname(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="birthdate">
                                <Form.Label>Birthdate</Form.Label>
                                <Form.Control value={birthdate} type="date" placeholder="Enter Birthdate" onChange={e => setBirtdate(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="phoneNumber">
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control value={phoneNumber} type="number" placeholder="Enter PhoneNumber" onChange={e => setPhoneNumber(e.target.value)} />
                            </Form.Group>                           
                        </Form>
                    </Row>
                    <Row><Button onClick={createProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                    {error && <Row><Col>{String(error)}</Col></Row>}

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
    const [birthdate, setBirtdate] = useState();
    const [phoneNumber, setPhoneNumber] = useState();
    const [error, setError] = useState();

    let updateProfile = async () => {
        let profile = { email: email, name: name, surname: surname, birthdate: birthdate, phoneNumber: phoneNumber }
        await API.updateProfile(profile)
            .then(data => { profile = data; setProfile(profile)})
            .catch(error => setError(error));
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>PUT /API/profiles/:emailAddress</p></Row>
                    <Form>
                            <Form.Group className="mb-3" controlId="email">
                                <Form.Label>Email</Form.Label>
                                <Form.Control value={email} placeholder="Enter email" onChange={e => setEmail(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="name">
                                <Form.Label>Name</Form.Label>
                                <Form.Control value={name} placeholder="Enter name" onChange={e => setName(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="surname">
                                <Form.Label>Surname</Form.Label>
                                <Form.Control value={surname} placeholder="Enter surname" onChange={e => setSurname(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="birthdate">
                                <Form.Label>Birthdate</Form.Label>
                                <Form.Control value={birthdate} type="date" placeholder="Enter Birthdate" onChange={e => setBirtdate(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="phoneNumber">
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control value={phoneNumber} type="number" placeholder="Enter PhoneNumber" onChange={e => setPhoneNumber(e.target.value)} />
                            </Form.Group>                           
                        </Form>
                    <Row><Button onClick={updateProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                    {error && <Row><Col>{String(error)}</Col></Row>
                    }

                </Col>
            </Row>
        </Container>
    </>
}