import { Container, Tab, Tabs } from "react-bootstrap";
import { TicketTab } from "../components/TicketTab";
import { ProductsTab } from "../components/ProductsTab";

export function Dashboard(props) {

  console.log("DASHBOARD", props.tickets)

  return (
    <Container>
      <Tabs>
        <Tab eventKey="tickets" title="Tickets">
          <TicketTab handleCreate={props.handleCreate} tickets={props.tickets} products={props.products} />
        </Tab>
        {props.role === "CUSTOMER" &&
          <Tab eventKey="products" title="Products">
            <ProductsTab products={props.products} />
         </Tab>}
          </Tabs>
					

				</Container>
  );
}

