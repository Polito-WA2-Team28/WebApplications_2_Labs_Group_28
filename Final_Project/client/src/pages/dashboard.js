import { Container, Tab, Tabs } from "react-bootstrap";
import { CustomerTicketTab, ExpertTicketTab, ManagerTicketTab } from "../components/TicketTab";
import { ProductsTab } from "../components/ProductsTab";
import { Roles } from "../model/rolesEnum";
import ExpertsInfoTab from "../components/expertsInfoTab";

export function Dashboard(props) {

  const tickets = props.tickets;
  const products = props.products;
  const handleCreate = props.handleCreate;
  const role = props.role;

  return (
    <Container>
      {role === Roles.CUSTOMER && <CustomerTabs handleCreate={handleCreate} tickets={tickets} products={products} />}
      {role === Roles.EXPERT && <ExpertsTab tickets={tickets} />}
      {role === Roles.MANAGER && <ManagersTab tickets={tickets} />}
    </Container>
  );
}

function CustomerTabs(props) {
  return <Tabs>
    <Tab eventKey="tickets" title="Tickets">
      <CustomerTicketTab handleCreate={props.handleCreate} tickets={props.tickets} products={props.products} />
    </Tab>
    <Tab eventKey="products" title="Products">
      <ProductsTab />
    </Tab>
  </Tabs>

}

function ExpertsTab(props) {
  return (
    <Tabs>
      <Tab eventKey="tickets" title="Tickets">
        <ExpertTicketTab tickets={props.tickets} />
      </Tab>
    </Tabs>
  )
}

function ManagersTab(props) {
  return <Tabs>
    <Tab eventKey="tickets" title="Tickets">
    <ManagerTicketTab tickets={props.tickets} />
    </Tab>
    <Tab eventKey="experts" title="Experts">
      <ExpertsInfoTab experts={props.experts} />
      </Tab>
  </Tabs>
}
