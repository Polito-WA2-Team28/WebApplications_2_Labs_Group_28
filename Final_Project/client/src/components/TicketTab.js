import { faUpRightAndDownLeftFromCenter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button, Card, Col, Container, Modal, Row } from "react-bootstrap";

export function TicketTab(props) {
    return <>
        <CreationModal show={props.showCreate} handleClose={() => props.setShowCreate(false)} handleCreate={props.handleCreate} />
        <TicketListTable
            handleCreate={() => props.setShowCreate(true)}
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
        <Col className="mb-5">
            <Row>
              {props.tickets.number === 0 ? <EmptySearch /> : 
                 props.tickets.map((ticket) =>  <Col key={ticket}><TicketItem ticket={ticket}/></Col>)}
            </Row>
        </Col>
        </Row>
      </>
  );
  }
  
  function TicketItem(props) {
    return <Col className="mt-3">
    <Card>
      <Card.Body>
        <Card.Title>
          <Row className="top-row">
            <Col xs={8} sm={9}>
              {props.ticket.title.length > 25
                ? props.ticket.title.trim().slice(0, 24).concat("...")
                : props.ticket.title}
            </Col>
            <Col className="text-end">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => {}}
              >
                <FontAwesomeIcon icon={faUpRightAndDownLeftFromCenter} />
              </Button>
            </Col>
          </Row>
        </Card.Title>
      </Card.Body>
    </Card>
  </Col>
  }
  
  function EmptySearch() {
      return (
          <Container className="mt-5">
              <Row className="d-flex justify-content-center text-center">
                  Sorry there are no results
              </Row>
          </Container>
      );
  }
  
  function CreationModal(props) {
    
    const handleCreate = () => {
      const ticket = {}
      props.handleCreate(ticket);
      props.handleClose();
    }
  
    return <Modal show={props.show} onHide={props.handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Create a ticket</Modal.Title>
      </Modal.Header>
      <Modal.Body>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={props.handleClose}>Close</Button>
        <Button variant="primary" onClick={handleCreate}>Create</Button>
      </Modal.Footer>
    </Modal>
  }