import { Button } from "react-bootstrap";
import { useParams } from "react-router-dom";

export default function TicketPage(props) {
    const { ticketId } = useParams();

    const ticket = props.getTicket(ticketId);

    const closeTicket = () => {
        props.closeTicket(ticketId);
    }

    if (!ticket) return <h1>404: Ticket not found</h1>
    
    return (
        <div>
            <h1>Ticket Page</h1>
            <p>{ticket.ticketId}</p>
            <p>{ticket.ticketState}</p>
            <p>{ticket.description}</p>
            <p>{ticket.serialNumber}</p>
            {
                ticket.ticketState === "OPEN" &&
                <Button variant="primary" onClick={closeTicket()}>Close Ticket</Button>
            }
            

        </div>
    )
}
