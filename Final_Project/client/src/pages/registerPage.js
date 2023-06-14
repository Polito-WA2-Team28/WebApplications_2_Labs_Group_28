import { useState } from "react";
import { Col, Container, Form, Row, Button, Card, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import CustomerRegistrationForm from "../model/customerRegistrationForm";

export function RegisterPage(props) {

    const navigate  = useNavigate();

    let [message, setMessage] = useState("");


    const handleRegistration = (credentials) => {
        try {
            const profile = new CustomerRegistrationForm
                (credentials.name, credentials.surname,
                    credentials.username, dayjs(),
                    credentials.birthDate, credentials.email,
                    credentials.phone, credentials.password);
            props.handleRegistration(profile);
            navigate("/dashboard");
        }
        catch (error) {
            console.error(error);
        }
    }

  return (
    <Container className="login-form">
    <Row>
        <h1>Register</h1>
    </Row>
    <Row>
        <Col className="col-centered">
            <Card style={{ marginTop: 5,marginBottom: 10 }}>
                <Card.Body>
                    <RegistrationForm
                        message={message}
                        setMessage={setMessage}
                        register={handleRegistration}
                    />
                </Card.Body>
            </Card>
        </Col>
    </Row>
</Container>
    );
}

function RegistrationForm(props) {
	const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [username, setUsername] = useState('');
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const [repeatPwd, setRepeatPwd] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    
	const handleSubmit = (event) => {
		let credentials = { name, surname, username, email, phoneNumber, password };
		let invalids = [];

		event.preventDefault();

		props.setMessage('');

		let validateName = (text) => {
			return !String(text).match(/[^a-zA-Z]/i);
		};

		let validatePhone = (phone) => {
			return !String(phone).match(/[^0-9]/i);
        };
        
        
		if (name === '' || !validateName(name)) {
			invalids.push(" name");
			setName('');
		}
		if (surname === '' || !validateName(surname)) {
			invalids.push(" surname");
			setSurname('');
        }
        if (username === '') {
            invalids.push(" username");
            setUsername('');
        }
		if (email === '' ) {
			invalids.push(" email");
			setEmail('');
		}
		if (phoneNumber === 0 || !validatePhone(phoneNumber)) {
			invalids.push(" phone");
			setPhoneNumber('');
		}
		if (password === '' || password !== repeatPwd) {
			invalids.push(" password");
		}

		if (invalids.length !== 0 || !props.register(credentials)){
			props.setMessage(`Invalid${invalids.toString()}`);
			setPassword('');
			setRepeatPwd('');
		}
	};

	let emptyMessage=function(){props.setMessage('')}

	let selectName = function(ev){setName(ev.target.value)}
	let selectSurname = function(ev){setSurname(ev.target.value)}
	let selectEmail = function(ev){setEmail(ev.target.value)}
	let selectPhoneNumber = function(ev){setPhoneNumber(parseInt(ev.target.value))}
	let selectUsername=function(ev){setUsername(ev.target.value)}
	let selectPassword = function(ev){setPassword(ev.target.value)}
	let selectRepeatPwd = function(ev){setRepeatPwd(ev.target.value)}


	return (
		<Container>
			<Row>
				<Col>
					<Form>
						{props.message && <Alert variant='danger' onClose={emptyMessage} dismissible>{props.message}</Alert>}
						<Form.Group controlId='name'>
							<Form.Label>Name</Form.Label>
							<Form.Control type='text' value={name} onChange={selectName} />
						</Form.Group>
						<Form.Group controlId='surname'>
							<Form.Label>Surname</Form.Label>
							<Form.Control type='text' value={surname} onChange={selectSurname} />
                        </Form.Group>
                        <Form.Group controlId='username'>
							<Form.Label>Surname</Form.Label>
							<Form.Control type='text' value={username} onChange={selectUsername} />
						</Form.Group>
						<Form.Group controlId='email'>
							<Form.Label>E-mail</Form.Label>
							<Form.Control type='email' value={email} onChange={selectEmail} />
						</Form.Group>
						<Form.Group controlId='phoneNumber'>
							<Form.Label>Phone</Form.Label>
							<Form.Control type='text' value={phoneNumber} onChange={selectPhoneNumber} />
						</Form.Group>
						<Form.Group controlId='password'>
							<Form.Label>Password</Form.Label>
							<Form.Control type='password' value={password} onChange={selectPassword} />
						</Form.Group>
						<Form.Group controlId='repeatPwd'>
							<Form.Label>Repeat password</Form.Label>
							<Form.Control type='password' value={repeatPwd} onChange={selectRepeatPwd} />
						</Form.Group>
						<Button style={{ marginTop: 20 }} type="submit" onClick={handleSubmit}>Register</Button>
					</Form>
				</Col>
			</Row>
		</Container>
	)
}