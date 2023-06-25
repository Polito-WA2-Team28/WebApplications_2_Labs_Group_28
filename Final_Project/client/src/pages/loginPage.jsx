import { useContext, useState } from 'react'
import { Form, Button, Alert, Card } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import '../styles/loginPage.css'
import { ActionContext } from '../Context'

export default function LoginPage(props) {
  const navigate = useNavigate()

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
        <h1 style={{ textAlign: 'center', paddingTop: '10px' }}>Login</h1>
        <Card.Body>
          <LoginForm />
          <p style={{ textAlign: 'center', marginTop: 5 }}>
            Not a member?{' '}
            <Button
              variant="link"
              style={{ padding: 0 }}
              onClick={() => navigate('/register')}
            >
              Register
            </Button>
          </p>
        </Card.Body>
      </Card>
    </div>
  )
}

function LoginForm(props) {
  let navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [message, setMessage] = useState(null)

  const { handleLogin } = useContext(ActionContext)

  const handleSubmit = async (event) => {
    event.preventDefault()
    const credentials = { username, password }
    let invalids = []
    if (username === '') invalids.push(' username')
    if (password === '') invalids.push(' password')
    if (invalids.length === 0) {
      await handleLogin(credentials)
        .then(() => navigate('/dashboard'))
        .catch((error) => setMessage(error.message))
    } else setMessage('Invalid' + invalids)
  }

  return (
    <Form>
      {message && (
        <Alert variant="danger" onClose={() => setMessage(null)} dismissible>
          {message}
        </Alert>
      )}
      <Form.Group controlId="username">
        <Form.Label>E-mail</Form.Label>
        <Form.Control
          type="email"
          value={username}
          onChange={(ev) => setUsername(ev.target.value)}
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
      <Button
        className="custom-button-login"
        type="submit"
        onClick={handleSubmit}
      >
        Login
      </Button>
    </Form>
  )
}
