

import { Row } from "react-bootstrap";
export default function LandingPage(props) {
	return (
		<div className="text-center">
			<img
				src="https://freepngimg.com/thumb/wrench/3-2-wrench-picture.png"
				width="200px"
				className="mt-3"
				alt="alt"
			/><br />
			<Row className="my-3">
				<span className="d-inline-block">
					Your <span className="fw-bold">BEST COMPANION</span> for
				</span>
			</Row>
			<Row>
				<span className="d-inline-block">
					the technical support you need!
				</span>
			</Row>
		</div>
	);
}