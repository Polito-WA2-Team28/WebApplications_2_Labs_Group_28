import { Button, Card, Col, Row, Container, Form } from 'react-bootstrap'
import { useParams } from 'react-router-dom'
import { useState } from 'react'
import '../styles/TicketPage.css'

export default function TicketPage(props) {
  const { ticketId } = useParams()
  const [ticket, setTicket] = useState(null)
  const [messages, setMessages] = useState([])
  const [newMessage, setNewMessage] = useState('')

  const sendNewMessage = () => {
    try {
      props.sendMessage(ticketId, newMessage).then(() => {
        setNewMessage('')
        props.getMessages(ticketId).then((messages) => {
          if (messages.content != null) setMessages(messages.content)
        })
      })
    } catch (error) {
      console.error(error)
    }
  }

  ticket == null &&
    props.getTicket(ticketId).then((ticket) => {
      console.log(ticket)
      setTicket(ticket)
    })

  ticket != null &&
    props.getMessages(ticketId).then((messages) => {
      if (messages.content != null) setMessages(messages.content)
    })

  const closeTicket = () => {
    props.closeTicket(ticketId)
  }

  return (
    <>
      {ticket == null ? (
        <h1>Loading...</h1>
      ) : (
        <Card className="ticketPageCard" style={{ height: '70%' }}>
          <Card.Body>
            <Card.Title>Ticket Page</Card.Title>
            <Row style={{height: '100%'}}>
              <Col>
                <Card.Text>
                  <strong>Ticket ID:</strong> {ticket.ticketId}
                </Card.Text>
                <Card.Text>
                  <strong>Ticket State:</strong> {ticket.ticketState}
                </Card.Text>
                <Card.Text>
                  <strong>Description:</strong> {ticket.description}
                </Card.Text>
                <Card.Text>
                  <strong>Serial Number:</strong> {ticket.serialNumber}
                </Card.Text>
                {ticket.ticketState === 'OPEN' && (
                  <Button variant="primary" onClick={closeTicket}>
                    Close Ticket
                  </Button>
                )}
              </Col>
              <Col style={{ position: 'relative' }}>
                {messages != null && messages.length !== 0 ? (
                  <Col
                    style={{
                      overflowY: 'auto',
                      height: '80%',
                      marginBottom: '50px',
                    }}
                  >
                    {messages.map((message, index) => {
                      return (
                        <Card.Text key={index}>
                          <strong>{message.sender}:</strong>
                          {message.messageText}
                        </Card.Text>
                      )
                    })}
                  </Col>
                ) : (
                  <Card.Text>No messages yet</Card.Text>
                )}
                <Row
                  style={{
                    position: 'absolute',
                    bottom: "20px",
                  }}
                >
                  <Col>
                    <Form>
                      <Form.Group controlId="formBasicEmail">
                        <Form.Control
                          type="text"
                          placeholder="Enter message"
                          value={newMessage}
                          onChange={(ev) => setNewMessage(ev.target.value)}
                        />
                      </Form.Group>
                    </Form>
                  </Col>
                  <Col>
                    <Button onClick={sendNewMessage}>Send</Button>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      )}
    </>
  )
}
