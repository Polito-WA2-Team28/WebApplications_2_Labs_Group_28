import './App.css';
import { Tab, Tabs } from 'react-bootstrap';
import { GetProductById, GetProducts } from './components/products';
import { CreateProfile, GetProfileByEmailAddress, UpdateProfile } from './components/profiles';

function App() {
  return (
    <Tabs>
      <Tab eventKey="products" title="Products">
        <GetProducts />
      </Tab>
      <Tab eventKey="productById" title="ProductById">
        <GetProductById />
      </Tab>
      <Tab eventKey="getProfiles" title="GetProfiles">
        <GetProfileByEmailAddress />
      </Tab>
      <Tab eventKey="createProfiles" title="CreateProfiles">
        <CreateProfile />
      </Tab>
      <Tab eventKey="UpdateProfiles" title="UpdateProfiles">
        <UpdateProfile />
      </Tab>

    </Tabs>
  );
}

export default App;