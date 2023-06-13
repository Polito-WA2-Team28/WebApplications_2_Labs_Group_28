import { Navbar } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { Button } from "react-bootstrap";
import { Col } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";

function MyNavBar(props) {

    const navigate = useNavigate();
    const location = useLocation();

    return (
        <Navbar bg="dark" variant="dark" 
            style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                width: "100%"
            }}>
            <Container>
                <Navbar.Brand>TICKETING MANAGEMENT SYSTEM</Navbar.Brand>
                {props.user && <Button onClick={() => props.logout()}>Logout</Button>}
            </Container>
        </Navbar>
    )
}

export default MyNavBar;