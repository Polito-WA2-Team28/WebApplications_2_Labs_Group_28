import { useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { API } from "../API";

export function GetProfileByEmailAddress() {

    const [profile, setProfile] = useState([]);

    let getProfile = async () => {
        let products = [];
        await API.getProfileByEmailAddress()
            .then(data => { products = data; setProfile(products) })
            .catch(error => console.log(error));

        return products;
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>GET /API/profiles/:emailAddress</p></Row>
                    <Row><Button onClick={getProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                </Col>
            </Row>
        </Container>
    </>
}

export function CreateProfile() {

    const [profile, setProfile] = useState();

    let createProfile = async () => {
        let profile;
        await API.createProfile(profile)
            .then(data => { profile = data; setProfile(profile)})
            .catch(error => console.log(error));
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>POST /API/profiles/:emailAddress</p></Row>
                    <Row><Button onClick={createProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                </Col>
            </Row>
        </Container>
    </>
}

export function UpdateProfile() {

    const [profile, setProfile] = useState();

    let updateProfile = async () => {
        let profile;
        await API.updateProfile(profile)
            .then(data => { profile = data; setProfile(profile)})
            .catch(error => console.log(error));
    }

    return <>
        <Container>
            <Row>
                <Col>
                    <Row><p style={{ textAlign: 'center' }}>PUT /API/profiles/:emailAddress</p></Row>
                    <Row><Button onClick={updateProfile}>SEND REQUEST</Button></Row>
                    {profile && <Row>{profile.name}</Row>}
                </Col>
            </Row>
        </Container>
    </>
}