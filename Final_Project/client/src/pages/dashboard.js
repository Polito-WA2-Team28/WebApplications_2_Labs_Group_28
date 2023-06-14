import { faUpRightAndDownLeftFromCenter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { Button, Card, Col, Container, Form, Row, Tab, Tabs } from "react-bootstrap";

export function Dashboard(props) {

  const [tickets, setTickets] = useState([]);

  return (
    <Container>
      <Tabs>
        <Tab eventKey="tickets" title="Tickets">
          <TicketListTable
            tickets={tickets}
          />
        </Tab>
        {props.user && props.user.type === "Customer" && <Tab eventKey="products" title="Products">
         </Tab>}
          </Tabs>
					

				</Container>
  );
}

function TicketListTable(props) {

  const tickets = props.tickets.map((ticket) => 
    <Col key={ticket}>
			<TicketItem
				ticket={ticket}
			/>
    </Col>);

return (
  <Row>
    <Col className="mb-5">
      <Row>
        {tickets.length === 0 ? <EmptySearch /> : tickets}
      </Row>
    </Col>
  </Row>
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