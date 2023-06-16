import "../styles/AppNavBar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faCircleUser,
	faRightToBracket,

} from "@fortawesome/free-solid-svg-icons";
import {
	Button,
	Col,
	Container,
	Navbar,
	Row,
	NavDropdown,
} from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export function AppNavBar(props) {
	let navigate = useNavigate();

	let handleHomeClick = () => {
		navigate("/");
	};

	let handleUserClick = () => {
		props.loggedIn ? navigate("/user") : navigate("/login");
	};

	let handleLogout = () => {
		props.logout();
		navigate("/");
	};

	const navDropdownTitleForUser = <FontAwesomeIcon icon={faCircleUser} />;

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
							<Button variant="navbar" size="xl" onClick={handleHomeClick}>
								TICKETING MANAGEMENT SYSTEM
							</Button>
						</Col>
						<Col
							xs={1}
							className="d-flex align-items-center justify-content-center"
						>
							{props.loggedIn ? (
								<NavDropdown variant="navbar"
									title={navDropdownTitleForUser}
									id="collasible-nav-dropdown"
									drop="start"
								>
									<NavDropdown.Item onClick={handleUserClick}>
										Profile
									</NavDropdown.Item>
									<NavDropdown.Divider />
									<NavDropdown.Item onClick={handleLogout}>
										Log Out
									</NavDropdown.Item>
								</NavDropdown>
							) : (
								<Button variant="navbar" onClick={handleUserClick}>
									<FontAwesomeIcon icon={faRightToBracket} />
								</Button>
							)}
						</Col>
					</Row>
				</Container>
			</Navbar>
			
		</>
	);
}