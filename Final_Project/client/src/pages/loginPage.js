import { useState } from "react";
import { Col, Container, Form, Row, Button, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export function LoginPage(props) {

    const navigate  = useNavigate();

    const handleLogin = async (credentials) => {
			await props.handleLogin(credentials)
			.then(() => { navigate("/dashboard") })
			.catch((error) => { console.log(error) })
    }

    const handleRegistration = () => {
        navigate("/register");
    }


    return (
        <Container className="login-form">
			<Row>
				<Col xs={3} />
				<Col xs={6} >
					<Row>
						<h1>Login</h1>
					</Row>
					<Row>
						<LoginForm
							message={props.message}
							setMessage={props.setMessage}
							login={handleLogin}
						/>
					</Row>
					<Row>
						<p style={{ textAlign: "center", marginTop: 5 }}>Not a member?
							{' '}<u><a onClick={handleRegistration} style={{ cursor: 'pointer' }}>Register</a></u>
						</p>
					</Row>
				</Col >
				<Col xs={3} />
			</Row>
		</Container>
    );
}

function LoginForm(props) {
	let navigate = useNavigate();
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');

	const handleSubmit = async (event) => {

		event.preventDefault();
		props.setMessage('');
		const credentials = { username, password };

		let invalids = [];
		if (username === '') {
			invalids.push(" username");
		}
		if (password === '') {
			invalids.push(" password");
		}
		if (invalids.length === 0) {
			let value
			await props.login(credentials)
			.then(val => {value = val})
			console.log("value", value)
			if (value === true)
				navigate("/");
		} else {
			props.setMessage(`Invalid${invalids.toString()}`);
		}
	};


	return (
		<Container>
			<Row>
				<Col>
					<Form>
						{props.message ? <Alert variant='danger' onClose={() => props.setMessage('')} dismissible>{props.message}</Alert> : ''}
						<Form.Group controlId='username'>
							<Form.Label>E-mail</Form.Label>
							<Form.Control type='email' value={username} onChange={ev => setUsername(ev.target.value)} />
						</Form.Group>
						<Form.Group controlId='password'>
							<Form.Label>Password</Form.Label>
							<Form.Control type='password' value={password} onChange={ev => setPassword(ev.target.value)} />
						</Form.Group>
						<Row>
							<Button style={{ marginTop: 20 }} type="submit" onClick={handleSubmit}>Login</Button>
						</Row>
					</Form>
				</Col>
			</Row>
		</Container>
	)
}