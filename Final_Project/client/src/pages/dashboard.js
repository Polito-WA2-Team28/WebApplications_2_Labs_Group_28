import { useState } from "react";
import { Container, Tab, Tabs } from "react-bootstrap";
import { TicketTab } from "../components/TicketTab";
import { ProductsTab } from "../components/ProductsTab";

export function Dashboard(props) {

  const [showCreate, setShowCreate] = useState(false);

  return (
    <Container>
      <Tabs>
        <Tab eventKey="tickets" title="Tickets">
          <TicketTab showCreate={showCreate} setShowCreate={setShowCreate} handleCreate={props.handleCreate} tickets={props.tickets} />
        </Tab>
        {props.role === "CUSTOMER" &&
          <Tab eventKey="products" title="Products">
            <ProductsTab />
         </Tab>}
          </Tabs>
					

				</Container>
  );
}

