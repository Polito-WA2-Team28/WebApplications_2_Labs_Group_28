import dayjs from 'dayjs'
import { useContext, useState } from 'react'
import { Form, Button, Card, Alert } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import { ActionContext } from '../Context'

export default function RegisterPage() {
  return (
    <div
      className="login-form"
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <Card style={{ marginTop: 25, marginBottom: 10, width: '60%' }}>
        <h1 style={{ textAlign: 'center', paddingTop: '10px' }}>Register</h1>
        <Card.Body>
          <RegistrationForm />
        </Card.Body>
      </Card>
    </div>
  )
}

function RegistrationForm() {
  const [name, setName] = useState('')
  const [surname, setSurname] = useState('')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [repeatPwd, setRepeatPwd] = useState('')
  const [phoneNumber, setPhoneNumber] = useState('')
  const [birthDate, setBirthDate] = useState('')
  const [message, setMessage] = useState('')

  const navigate = useNavigate()
  const { handleRegistration } = useContext(ActionContext)

  const handleSubmit = (event) => {
    event.preventDefault()
    let credentials = {
      name,
      surname,
      username,
      email,
      phoneNumber,
      password,
      birthDate,
      registrationDate: dayjs().format('YYYY-MM-DD'),
    }
    let invalids = []

    if (name === '' || String(name).match(/[^a-zA-Z]/i)) {
      invalids.push(' name')
      setName('')
    }
    if (surname === '' || String(surname).match(/[^a-zA-Z]/i)) {
      invalids.push(' surname')
      setSurname('')
    }
    if (username === '') {
      invalids.push(' username')
      setUsername('')
    }
    if (email === '') {
      invalids.push(' email')
      setEmail('')
    }
    if (phoneNumber === 0 || String(phoneNumber).match(/[^0-9]/i)) {
      invalids.push(' phone')
      setPhoneNumber('')
    }
    if (password === '' || password !== repeatPwd) {
      invalids.push(' password')
    }
    if (birthDate === '') {
		invalids.push(' birthdate')
      setBirthDate('')
    }

    if (invalids.length !== 0) {
      setMessage(`Invalid${invalids.toString()}`)
    } else
      handleRegistration(credentials)
        .then(() => navigate('/login'))
        .catch((error) => setMessage(error.message))
  }

  return (
    <Form>
      {message && (
        <Alert variant="danger" onClose={() => setMessage('')} dismissible>
          {message}
        </Alert>
      )}
      <Form.Group controlId="name">
        <Form.Label>Name</Form.Label>
        <Form.Control
          type="text"
          value={name}
          onChange={(ev) => setName(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="surname">
        <Form.Label>Surname</Form.Label>
        <Form.Control
          type="text"
          value={surname}
          onChange={(ev) => setSurname(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="username">
        <Form.Label>Username</Form.Label>
        <Form.Control
          type="text"
          value={username}
          onChange={(ev) => setUsername(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="email">
        <Form.Label>Birthdate</Form.Label>
        <Form.Control
          type="date"
          value={birthDate}
          onChange={(ev) => setBirthDate(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="email">
        <Form.Label>E-mail</Form.Label>
        <Form.Control
          type="email"
          value={email}
          onChange={(ev) => setEmail(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="phoneNumber">
        <Form.Label>Phone</Form.Label>
        <Form.Control
          type="text"
          value={phoneNumber}
          onChange={(ev) => setPhoneNumber(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="password">
        <Form.Label>Password</Form.Label>
        <Form.Control
          type="password"
          value={password}
          onChange={(ev) => setPassword(ev.target.value)}
        />
      </Form.Group>
      <Form.Group controlId="repeatPwd">
        <Form.Label>Repeat password</Form.Label>
        <Form.Control
          type="password"
          value={repeatPwd}
          onChange={(ev) => setRepeatPwd(ev.target.value)}
        />
      </Form.Group>
      <Button
        className="custom-button-login"
        type="submit"
        onClick={handleSubmit}
      >
        Register
      </Button>
    </Form>
  )
}
