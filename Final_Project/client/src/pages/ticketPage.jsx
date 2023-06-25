import { Button, Card, Col, Row, Form } from 'react-bootstrap'
import { useParams } from 'react-router-dom'
import { useContext, useState } from 'react'
import '../styles/TicketPage.css'
import { ActionContext, UserContext } from '../Context'
import Roles from '../model/rolesEnum'
import TicketState from '../model/TicketState'

export default function TicketPage() {
  const { ticketId } = useParams()
  const [ticket, setTicket] = useState(null)
  const [messages, setMessages] = useState([])
  const [newMessage, setNewMessage] = useState('')

  const { sendMessage, getMessages, getTicketByID } = useContext(ActionContext)
  const { role } = useContext(UserContext)

  
  const sendNewMessage = () => {
    sendMessage(ticketId, newMessage)
      .then(() => {
        setNewMessage('')
        getMessages(ticketId).then(
          messages =>
          messages && messages.content != null && setMessages(messages.content),
        )
      })
      .catch((error) => console.error(error))
  }

  ticket == null &&
    getTicketByID(ticketId).then((ticket) => {
      console.log(ticket)
      setTicket(ticket)
    })

  ticket != null &&
    getMessages(ticketId).then((messages) => {
      console.log(messages)
      if (messages && messages.content != null) setMessages(messages.content)
    })

  return (
    <>
      {ticket == null ? (
        <h1>Loading...</h1>
      ) : (
        <Card className="ticketPageCard" style={{ height: '70%' }}>
          <Card.Body>
            <Card.Title>Ticket Page</Card.Title>
            <Row style={{ height: '100%' }}>
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
                <Row>
                  {role === Roles.CUSTOMER && (
                    <CustomerButton ticket={ticket} />
                  )}
                  {role === Roles.EXPERT && <ExpertButton ticket={ticket} />}
                  {role === Roles.MANAGER && <ManagerButton ticket={ticket} />}
                </Row>
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
                  {(role === Roles.CUSTOMER || role === Roles.EXPERT) &&
                    (<Row
                    style={{
                      position: 'absolute',
                      bottom: '20px',
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
                  </Row>)}
              </Col>
            </Row>
          </Card.Body>
        </Card>
      )}
    </>
  )
}

function CustomerButton(props) {
  const ticket = props.ticket

  const handleClose = () => {
    switch (ticket.ticketState) {
      case TicketState.OPEN:
        console.log('Customer close open ticket')
        break
      case TicketState.RESOLVED:
        console.log('Customer close resolved ticket')
        break
      case TicketState.REOPENED:
        console.log('Customer close reopened ticket')
        break
      default:
        console.error('Invalid ticket state')
    }
  }

  const handleReopen = () => {
    console.log('Customer reopen ticket')
  }


  return (
    <>
      <Col><Button
        variant="primary"
        disabled={![TicketState.OPEN, TicketState.REOPENED, TicketState.RESOLVED ].includes(ticket.ticketState) }
        onClick={handleClose}
      >
        Close Ticket
      </Button></Col>


      <Col><Button
        variant="primary"
        disabled={ticket.ticketState !== TicketState.CLOSED}
        onClick={handleReopen}
      >
        Reopen Ticket
      </Button></Col>
    </>
  )
}

function ExpertButton(props) {
  const ticket = props.ticket

  return (
      <Col><Button
        variant="primary"
        disabled={ticket.ticketState !== TicketState.IN_PROGRESS}
        onClick={() => console.log('Expert resolve ticket')}
      >
        Resolve Ticket
      </Button></Col>
  )
}

function ManagerButton(props) {
  const ticket = props.ticket

  const handleClose = () => {
    switch (ticket.ticketState) {
      case TicketState.OPEN:
        console.log('Manager close open ticket')
        break
      case TicketState.IN_PROGRESS:
        console.log('Manager close in progress ticket')
        break
      case TicketState.REOPENED:
        console.log('Manager close reopened ticket')
        break
      default:
        console.error('Invalid ticket state')
    }
  }

  const handleAssign = () => {
    console.log('Manager assign ticket')
  }

  const handleRelieve = () => {
    console.log('Manager relieve expert')
  }

  return (
    <>
      <Col><Button
        variant="primary"
        disabled={ticket.ticketState !== TicketState.OPEN}
        onClick={handleClose}
      >
        Close Ticket
      </Button></Col>

     <Col><Button
        variant="primary"
        disabled={ticket.ticketState !== TicketState.OPEN}
        onClick={handleAssign}
      >
        Assign Ticket
      </Button></Col> 

      <Col><Button
        variant="primary"
        disabled={ticket.ticketState !== TicketState.IN_PROGRESS}
        onClick={handleRelieve}
      >
        Relieve expert
      </Button></Col> 
    </>
  )
}
