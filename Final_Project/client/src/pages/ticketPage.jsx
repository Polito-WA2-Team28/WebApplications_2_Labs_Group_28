import { Button, Card, Col, Row, Form, Modal } from 'react-bootstrap'
import { useParams } from 'react-router-dom'
import { useContext, useEffect, useState } from 'react'
import '../styles/TicketPage.css'
import { ActionContext, UserContext } from '../Context'
import Roles from '../model/rolesEnum'
import TicketState from '../model/ticketState'

export default function TicketPage() {
  const { ticketId } = useParams()
  const [ticket, setTicket] = useState(null)
  const [messages, setMessages] = useState([])
  const [newMessage, setNewMessage] = useState('')
  const [dirty, setDirty] = useState(false)

  const { sendMessage, getMessages, getTicketByID } = useContext(ActionContext)
  const { role, experts } = useContext(UserContext)

  const sendNewMessage = () => {
    sendMessage(ticketId, newMessage)
      .then(() => {
        setNewMessage('')
        getMessages(ticketId).then((messages) => {
          console.log(messages)
          messages &&
            messages.content != null &&
            setMessages(
              messages.content.sort((a, b) =>
                a.timestamp.localeCompare(b.timestamp),
              ),
            )
        })
      })
      .catch((error) => console.error(error))
  }

  useEffect(() => {
    
      getTicketByID(ticketId).then((ticket) => {
        setTicket(ticket)
      })
    
      getMessages(ticketId).then((messages) => {
        if (messages && messages.content != null)
          setMessages(
            messages.content.sort((a, b) =>
              a.timestamp.localeCompare(b.timestamp),
            ),
          )
      })
    setDirty(false)
  }, [ticket, dirty])

  /* ticket == null &&
    getTicketByID(ticketId).then((ticket) => {
      setTicket(ticket)
    })

  ticket != null &&
    getMessages(ticketId).then((messages) => {
      if (messages && messages.content != null) setMessages(messages.content)
    }) */

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
                    <CustomerButton ticket={ticket} setDirty={setDirty} />
                  )}
                  {role === Roles.EXPERT && (
                    <ExpertButton ticket={ticket} setDirty={setDirty} />
                  )}
                  {role === Roles.MANAGER && (
                    <ManagerButton
                      ticket={ticket}
                      experts={experts}
                      setDirty={setDirty}
                    />
                  )}
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
                {(role === Roles.CUSTOMER || role === Roles.EXPERT) && (
                  <Row
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
                  </Row>
                )}
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

  const { customerReopenTicket } = useContext(ActionContext)

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

  return (
    <>
      <Col>
        <Button
          variant="primary"
          disabled={
            ![
              TicketState.OPEN,
              TicketState.REOPENED,
              TicketState.RESOLVED,
            ].includes(ticket.ticketState)
          }
          onClick={handleClose}
        >
          Close Ticket
        </Button>
      </Col>

      <Col>
        <Button
          variant="primary"
          disabled={ticket.ticketState !== TicketState.CLOSED}
          onClick={() => { customerReopenTicket(ticket.ticketId); props.setDirty(true)}}
        >
          Reopen Ticket
        </Button>
      </Col>
    </>
  )
}

function ExpertButton(props) {
  const ticket = props.ticket

  const { expertResolveTicket } = useContext(ActionContext)

  return (
    <Col>
      <Button
        variant="primary"
        disabled={ticket.ticketState !== TicketState.IN_PROGRESS}
        onClick={() => { expertResolveTicket(ticket.ticketId); props.setDirty(true)}}
      >
        Resolve Ticket
      </Button>
    </Col>
  )
}

function ManagerButton(props) {
  const ticket = props.ticket
  const experts = props.experts

  const [show, setShow] = useState(false)
  const {
    managerHandleCloseTicket,
    managerAssignExpert,
    managerRelieveExpert,
  } = useContext(ActionContext)

  return (
    <>
      <AssignExpertModal
        ticket={ticket}
        experts={experts}
        show={show}
        handleClose={() => setShow(false)}
        handleAssign={managerAssignExpert}
        setDirty = {props.setDirty}
      />
      <Col>
        <Button
          variant="primary"
          disabled={
            ![
              TicketState.OPEN,
              TicketState.IN_PROGRESS,
              TicketState.REOPENED,
            ].includes(ticket.ticketState)
          }
          onClick={() => { managerHandleCloseTicket(ticket); props.setDirty(true)}}
        >
          Close Ticket
        </Button>
      </Col>

      <Col>
        <Button
          variant="primary"
          disabled={ticket.ticketState !== TicketState.OPEN}
          onClick={() => setShow(true)}
        >
          Assign Ticket
        </Button>
      </Col>

      <Col>
        <Button
          variant="primary"
          disabled={ticket.ticketState !== TicketState.IN_PROGRESS}
          onClick={() => { managerRelieveExpert(ticket.ticketId);  props.setDirty(true)}}
        >
          Relieve expert
        </Button>
      </Col>
    </>
  )
}

function AssignExpertModal(props) {
  const experts = props.experts
  const ticket = props.ticket
  const [expert, setExpert] = useState(
    experts && experts.content && experts.content[0],
  )

  const assign = () => {
    props.handleAssign(ticket.ticketId, expert.id)
    props.setDirty(true)
    props.handleClose()
  }

  return (
    <Modal show={props.show} onHide={props.handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Assign Expert</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="exampleForm.ControlSelect1">
            <Form.Label>Expert</Form.Label>
            <Form.Control
              as="select"
              onSelect={(ev) => setExpert(ev.target.value)}
            >
              {experts &&
                experts.content &&
                experts.content.map((expert, index) => (
                  <option key={index} value={expert}>
                    {expert.email}
                  </option>
                ))}
            </Form.Control>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={props.handleClose}>
          Cancel
        </Button>
        <Button variant="primary" onClick={assign}>
          Assign
        </Button>
      </Modal.Footer>
    </Modal>
  )
}
