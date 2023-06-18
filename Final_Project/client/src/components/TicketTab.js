import { faUpRightAndDownLeftFromCenter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { Button, Card, CardGroup, Col, Form, Modal, Row } from "react-bootstrap";
import EmptySearch from "./EmptySearch";
import { useNavigate } from "react-router-dom";

export function CustomerTicketTab(props) {

  const [showCreate, setShowCreate] = useState(false);
  return <>
    <CreationModal show={showCreate} handleClose={() => setShowCreate(false)} handleCreate={props.handleCreate} products={props.products} />
    <Row>
      <Button onClick={() => setShowCreate(true)}>Create a ticket</Button>
    </Row>
    <TicketListTable tickets={props.tickets}
    />
  </>
}


export function ExpertTicketTab(props) {
  return (
    <h1>Expert Ticket Tab</h1>
  )
}

export function ManagerTicketTab(props) {
  return (
    <TicketListTable tickets={props.tickets} />
  )
}


function TicketListTable(props) {

  const tickets = props.tickets.content

  return (
    <Row>
      <CardGroup>
        {(tickets === undefined || tickets.length === 0) ? <EmptySearch /> :
          tickets.map((ticket) => <TicketItem key={ticket.ticketID} ticket={ticket} />)}
      </CardGroup>
    </Row>
  );
}

function TicketItem(props) {

  const navigate = useNavigate();

  return (
    <Card>
      <Card.Body>
        <Card.Title>
          <Row>
            <Col>{props.ticket.description}</Col>
            <Col className="text-end">
              <Button onClick={() => navigate(`/ticket/${props.ticket.ticketId}`)}>
                <FontAwesomeIcon icon={faUpRightAndDownLeftFromCenter} />
              </Button></Col>
          </Row>
        </Card.Title>
        <p>{props.ticket.ticketState}</p>
      </Card.Body>
    </Card>
  );
}

function CreationModal(props) {

  const handleCreate = () => {
    const ticket = { description, serialNumber }
    props.handleCreate(ticket);
    props.handleClose();
  }

  const [description, setDescription] = useState("");
  const [serialNumber, setSerialNumber] = useState("");

  return <Modal show={props.show} onHide={props.handleClose}>
    <Modal.Header closeButton>
      <Modal.Title>Create a ticket</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <Form>
        <Form.Group>
          <Form.Label>Description</Form.Label>
          <Form.Control type="text" placeholder="Enter description" value={description} onChange={ev => setDescription(ev.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>product</Form.Label>
          <Form.Select value={serialNumber} onChange={ev => setSerialNumber(ev.target.value)}>
            <option value="">Select a product</option>
            {props.products.map((product) => <option key={product.serialNumber} value={product.serialNumber}>{`${product.model} - ${product.deviceType}`}</option>)}
          </Form.Select>
        </Form.Group>
      </Form>
    </Modal.Body>
    <Modal.Footer>
      <Button variant="secondary" onClick={props.handleClose}>Close</Button>
      <Button variant="primary" onClick={handleCreate}>Create</Button>
    </Modal.Footer>
  </Modal>
}