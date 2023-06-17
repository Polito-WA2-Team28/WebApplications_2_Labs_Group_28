import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ToastContainer } from 'react-toastify';
import { LandingPage } from './pages/landingPage';
import { RegisterPage } from './pages/registerPage';
import { LoginPage } from './pages/loginPage';
import { Dashboard } from './pages/dashboard';
import { AppNavBar } from './components/AppNavBar';
import authAPI from './APIs/authAPI'
import { customerAPI } from './APIs/customerAPI';
import { UserPage } from './pages/userPage';
import jwt_decode from "jwt-decode";
import { productAPI } from './APIs/productAPI';
import TicketPage from './pages/ticketPage';

function App() {

  const [user, setUser] = useState(null);
  const [loggedIn, setLoggedIn] = useState(false);
  const [token, setToken] = useState(null);
  const [tickets, setTickets] = useState([]);
  const [products, setProducts] = useState([]);
  const [role, setRole] = useState(null)

  const handleLogin = async (credentials) => {
    await authAPI.login(credentials)
      .then((data) => {
        var decoded = jwt_decode(data);
        setRole(decoded.realm_access.roles[2])
        setToken(data);
        setLoggedIn(true);
      })
  };

  const handleRegistration = async (credentials) => {
    await authAPI.register(credentials)
      .then((data) => {
        setUser(data);
      })
  };

  const handleLogout = async () => {
    setToken(null);
    setLoggedIn(false);
    setUser(null);
    setRole(null);
  };

  const handleCreateTicket = async (ticket) => {
    await customerAPI.createTicket(token, ticket)
      .then((data) => {
        setTickets((prev) => [...prev, data]);
      })
  };


  useEffect(() => {
    const checkAut = async () => {
      await customerAPI.getProfile(token)
        .then((user) => setUser(() => user))
        .catch((err) => {
          if (err !== "Not authenticated") {
            console.error("error in getProfile: ", err)
          }
        });
    }
    if (role === "CUSTOMER")
      checkAut();
  }, [token, role])

  useEffect(() => {
    const getTickets = async () => {
      await customerAPI.getTickets(token)
        .then(tickets => {
          setTickets(() => tickets.content);
        })
        .catch((err) => {
          console.error("error in getTickets:", err);
        });
    }
    getTickets();
  }, [loggedIn, token])

  useEffect(() => {
    const getProducts = async () => {
      await productAPI.getProducts(token)
        .then(products => {
          setProducts(() => products);
        })
        .catch((err) => {
          console.error("error in getTickets:", err);
        });
    }
    if (role === "CUSTOMER")
      getProducts();
  }, [loggedIn, token, role])


  const getTicket = (ticketId) => {

    for (let ticket of tickets) {
      if (ticket.ticketId === Number.parseInt(ticketId)) {
        return ticket;
      }
    }
    return undefined

  }

  const closeTicket = async (ticketId) => {
    ticketId = Number.parseInt(ticketId)
    await customerAPI.compileSurvey(token, ticketId)
      .then(() => {
        setTickets((prev) => prev.filter((ticket) => ticket.ticketId !== ticketId))})
      .catch((err) => { console.error("error in closeTicket:", err) }
    )
  }


  return (<><BrowserRouter>
    <AppNavBar loggedIn={loggedIn} user={user} logout={handleLogout} />
    <Routes>
      <Route path="/" element={user ? <Navigate to={"/dashboard"} /> : <LandingPage />} />
      <Route path="/register" element={<RegisterPage handleRegistration={handleRegistration} />} />
      <Route path="/login" element={<LoginPage handleLogin={handleLogin} />} />
      <Route path="/dashboard"
        element={
          loggedIn ?
            <Dashboard user={user} tickets={tickets} products={products} role={role} handleCreate={handleCreateTicket} />
            : <Navigate to={"/"} />} />
      <Route path="/user" element={loggedIn ? <UserPage user={user} /> : <Navigate to={"/"} />} />
      <Route path="/ticket/:ticketId" element={loggedIn ?
        <TicketPage getTicket={getTicket} closeTicket={closeTicket} /> : <Navigate to={"/"} />} />
      <Route path="*" element={<h1 >Not Found</h1>} />
    </Routes>
  </BrowserRouter>
    <ToastContainer />
  </>
  );
}

export default App;
