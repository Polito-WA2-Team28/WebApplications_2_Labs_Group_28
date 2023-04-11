import { useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { API } from "../API";
import { ProfileModal } from "./ProfileModal";
import { errorHandler } from "./ErrorHandler";
import dayjs from "dayjs";

export function GetProfileByEmailAddress() {

    const [profile, setProfile] = useState([]);
    const [show, setShow] = useState(false);
    const [email, setEmail] = useState("");

    let getProfile = async () => {
        await API.getProfileByEmailAddress(email)
            .then(profile => { setProfile(profile); setShow(true) })
            .catch(error => errorHandler(error));
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>FIND ALL PROFILES</p></Row>
                    <Row>
                    <Form>
                            <Form.Group className="mb-3" controlId="email">
                                <Form.Label>Email</Form.Label>
                                <Form.Control value={email} placeholder="Enter email" onChange={e => setEmail(e.target.value)} />
                        </Form.Group>
                        </Form>
                    </Row>
                    <Row><Button onClick={getProfile}>SEND REQUEST</Button></Row>
                    <ProfileModal show={show} profile={profile} handleClose={() => setShow(false)} />
                </Col>
            </Row>
            
        </Container>
    </>
}

export function CreateProfile() {

    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [birthDate, setBirtdate] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [footer, setFooter] = useState("");

    let createProfile = async () => {
        let profile = {
            email: email,
            name: name,
            surname: surname,
            birthDate: dayjs(birthDate).format("YYYY-MM-DD").toString(),
            phoneNumber: phoneNumber
        }
        await API.createProfile(profile)
            .then(_ =>  setFooter("Profile created") )
            .catch(error => { setFooter(false); errorHandler(error);});
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>CREATE A PROFILE</p></Row>
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
                            <Form.Group className="mb-3" controlId="birthDate">
                                <Form.Label>birthDate</Form.Label>
                                <Form.Control value={birthDate} type="date" placeholder="Enter birthDate" onChange={e => setBirtdate(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="phoneNumber">
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control value={phoneNumber} type="number" placeholder="Enter PhoneNumber" onChange={e => setPhoneNumber(e.target.value)} />
                            </Form.Group>                           
                        </Form>
                    </Row>
                    <Row><Button onClick={createProfile}>SEND REQUEST</Button></Row>
                    {footer && <Row>{footer}</Row>}

                </Col>
            </Row>
            
        </Container>
    </>
}

export function UpdateProfile() {

    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [birthDate, setBirtdate] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [footer, setFooter] = useState("");

    let updateProfile = async () => {
        let profile = {
            name: name,
            surname: surname,
            birthDate: dayjs(birthDate).format("YYYY-MM-DD").toString(),
            phoneNumber: phoneNumber
        }
        await API.updateProfile(email,profile)
            .then( _ =>  setFooter("Profile updated"))
            .catch(error => {setFooter(false); errorHandler(error);});
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>EDIT A PROFILE</p></Row>
                    <Form>
                            <Form.Group className="mb-3" controlId="email">
                                <Form.Label>Email</Form.Label>
                            <Form.Control value={email} placeholder="Enter email" onChange={e => setEmail(e.target.value)}/>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="name">
                                <Form.Label>Name</Form.Label>
                                <Form.Control value={name} placeholder="Enter name" onChange={e => setName(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="surname">
                                <Form.Label>Surname</Form.Label>
                                <Form.Control value={surname} placeholder="Enter surname" onChange={e => setSurname(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="birthDate">
                                <Form.Label>birthDate</Form.Label>
                                <Form.Control value={birthDate} type="date" placeholder="Enter birthDate" onChange={e => setBirtdate(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="phoneNumber">
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control value={phoneNumber} type="number" placeholder="Enter PhoneNumber" onChange={e => setPhoneNumber(e.target.value)} />
                            </Form.Group>                           
                        </Form>
                    <Row><Button onClick={updateProfile}>SEND REQUEST</Button></Row>
                    {footer && <Row>{footer}</Row>}
                </Col>
            </Row>
            
        </Container>
    </>
}