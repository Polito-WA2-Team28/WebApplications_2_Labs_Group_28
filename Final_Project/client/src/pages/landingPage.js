import { Container, Button, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "./landingPage.css"



function LandingPage() {

  const navigate = useNavigate();

  return (
    <Container fluid>
          <img src="https://i.imgur.com/AD3MbBi.jpeg"
              alt="logo"
              style={{
                  width: "100%",
                  height: "auto",
                  position: "absolute",
                  zIndex: "-1",
                  opacity: "0.5"
                }}
      />
      <Row style={{ justifyContent: "center", alignItems: "center", paddingTop: "50%" }}>
        <Button onClick={() => navigate("/login")}>LOGIN</Button></Row>
    </Container>
  );
}

export default LandingPage;