

import { Row,  Container } from "react-bootstrap";
export function LandingPage(props) {
	return (
		<Container className="text-center">
			<img src="https://freepngimg.com/thumb/wrench/3-2-wrench-picture.png" width="200px" className="mt-3" alt="alt"/><br/>
			<CatchPhrase />
			<WelcomePhrase user={props.user} />
		</Container>
	);
}

function CatchPhrase() {
	return (
		<div style={{ fontSize: "3rem" }}>
			<Row>
				<span>
					Your <span className="fw-bold">BEST COMPANION</span> for
				</span>
			</Row>
			<Row>
				<span>
					the technical support you need!
				</span>
			</Row>
		</div>
	);
}

function WelcomePhrase(props) {
	let component;

	if (props.user) {
		component = (
			<div className="mt-4" style={{ fontSize: "2rem" }}>
				Welcome <span className="fw-bold"> {props.user.name.toUpperCase()}</span>!
			</div>
		);
	} else {
		component = false;
	}

	return component;
}