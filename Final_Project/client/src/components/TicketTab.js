import { faUpRightAndDownLeftFromCenter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { Button, Card, CardGroup, Col, Container, Form, Modal, Row } from "react-bootstrap";
import EmptySearch from "./EmptySearch";

export function TicketTab(props) {

  const [showCreate, setShowCreate] = useState(false);


  console.log("TEST", props.tickets)

    return <>
      <CreationModal show={showCreate} handleClose={() => setShowCreate(false)} handleCreate={props.handleCreate} products={props.products} />
        <TicketListTable
            handleCreate={() => setShowCreate(true)}
            tickets={props.tickets}
          />
</>
    
}

function TicketListTable(props) {

  
    return (
      <>
        <Row>
            <Button onClick={props.handleCreate}>Create a ticket</Button>
        </Row>
        <Row>
          <CardGroup>
              {props.tickets.length === 0 ? <EmptySearch /> : 
              props.tickets.map((ticket) => <TicketItem key={ticket.ticketID} ticket={ticket}/>)}
        </CardGroup>
        </Row>
      </>
  );
  }
  
  function TicketItem(props) {
    return <Col className="mt-3">
    <Card>
      <Card.Body>
        <Card.Title>
          {props.ticket.description}
        </Card.Title>
      </Card.Body>
    </Card>
  </Col>
  }
  
  function CreationModal(props) {
    
    const handleCreate = () => {
      const ticket = { description, serialNumber }
      console.log("TICKET", ticket)
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
            <Form.Control type="text" placeholder="Enter description" value={description} onChange={ev => setDescription(ev.target.value)}/>
          </Form.Group>
          <Form.Group>
            <Form.Label>product</Form.Label>
            <Form.Select value={serialNumber} onChange={ev => setSerialNumber(ev.target.value)}>
              <option value="">Select a product</option>
              {props.products.map((product) => <option key={product.serialNumber} value={product.serialNumber}>{`${product.model} - ${product.deviceType}` }</option>)}
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