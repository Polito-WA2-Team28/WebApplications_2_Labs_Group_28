import "../styles/AppNavBar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faArrowDown,
	faCircleUser,
	faRightToBracket,

} from "@fortawesome/free-solid-svg-icons";
import {
	Button,
	Col,
	Container,
	Navbar,
	Row,
} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { ActionContext, UserContext } from "../Context";

export default function AppNavBar(props) {

	const { role, loggedIn } = useContext(UserContext)
	const { handleLogout } = useContext(ActionContext)

	let navigate = useNavigate();

	let handleHome = () => {
		navigate("/");
	};

	let handleLogin = () => {
		navigate("/login");
	};

	let myHandleLogout = () => {
		handleLogout();
		navigate("/");
	};

	let handleUser = () => {
		navigate("/user");
	};

	const componentIfCustomer =
		<>
			<Col>
				<Button variant="navbar" onClick={handleUser}> <FontAwesomeIcon icon={faCircleUser} /></Button>
			</Col >
			<Col>
				<Button variant="navbar" onClick={myHandleLogout}><FontAwesomeIcon icon={faArrowDown} /></Button>
			</Col >
		</>

	const componentIfLogged = <Col>
		<Button variant="navbar" onClick={myHandleLogout}><FontAwesomeIcon icon={faArrowDown} /></Button>
	</Col >

	const componentIfNotLogged = <Button variant="navbar" onClick={handleLogin}><FontAwesomeIcon icon={faRightToBracket} /></Button>



	return (
		<>
			<Navbar bg="transparent" className="top-bg font-weight-bold">
				<Container fluid className="d-flex justify-content-center">
					<Row className="navbar-items d-flex justify-content-between">
						<Col
							xs={1}
							className="d-flex align-items-center justify-content-center"
						>

						</Col>
						<Col className="d-flex align-items-center justify-content-center">
							<Button variant="navbar" size="xl" onClick={handleHome}>
								TICKETING MANAGEMENT SYSTEM
							</Button>
						</Col>
						<Col
							xs={1}
							className="d-flex align-items-center justify-content-center"
						>
							{loggedIn ? (
								role === "CUSTOMER" ? componentIfCustomer
									: componentIfLogged) : componentIfNotLogged}
						</Col>
					</Row>
				</Container>
			</Navbar>

		</>
	);
}