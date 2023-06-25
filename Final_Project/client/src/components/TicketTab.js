import { faUpRightAndDownLeftFromCenter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useContext } from "react";
import { Button, Card, CardGroup, Col, Row} from "react-bootstrap";
import EmptySearch from "./EmptySearch";
import { useNavigate } from "react-router-dom";
import "../styles/TicketTab.css"
import { UserContext } from "../Context";

export default function TicketTab() {

  const ticketsPage = useContext(UserContext).tickets
  const tickets = ticketsPage.content

  return (
      <CardGroup>
        {(tickets === undefined || tickets.length === 0) ? <EmptySearch /> :
          tickets.map((ticket) => <TicketItem key={ticket.ticketID} ticket={ticket} />)}
      </CardGroup>
  );
}

function TicketItem(props) {

  const navigate = useNavigate();

  return (
    <Card className="ticketCard">
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