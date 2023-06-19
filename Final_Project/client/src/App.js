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
import { expertAPI } from './APIs/expertAPI';
import { managerAPI } from './APIs/managerAPI';
import { UserPage } from './pages/userPage';
import jwt_decode from "jwt-decode";
import TicketPage from './pages/ticketPage';
import { Roles } from './model/rolesEnum';
import { successToast, errorToast } from './components/toastHandler';
import { EditUserPage } from './pages/editUserPage';

function App() {

  const [user, setUser] = useState(null);
  const [loggedIn, setLoggedIn] = useState(false);
  const [token, setToken] = useState(null);
  const [tickets, setTickets] = useState([]);
  const [products, setProducts] = useState([]);
  const [role, setRole] = useState(null)
  const [flag, setFlag] = useState(false)

  const handleLogin = async (credentials) => {
    await authAPI.login(credentials)
      .then((data) => {
        var decoded = jwt_decode(data);
        const newRole = decoded.resource_access["ticketing-service-client"].roles[0]
        setRole(newRole)
        setToken(data);
        setLoggedIn(true);
        setFlag(true)
        successToast("Logged in successfully")
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
    successToast("Logged out successfully")
  };

  const getTicket = async (ticketId) => {
    return await customerAPI.getTicket(token, ticketId)
      .then((ticket) => { return ticket })
      .catch((err) => errorToast(err));
  }



  const handleCreateTicket = async (ticket) => {
    await customerAPI.createTicket(token, ticket)
      .then(() => {
        setFlag(true)
      })
  };

  const handleEditProfile = async (profile) => {
    await customerAPI.patchProfile(token, profile)
      .then((user) => {
        setUser(profile)
        successToast("Changes saved!")
      })
      .catch((err) => {
        errorToast(err)
      });

  };

  useEffect(() => {
    const checkAut = async () => {
      await customerAPI.getProfile(token)
        .then((user) => setUser(() => user))
        .catch((err) => {
          if (err !== "Not authenticated") {
            errorToast(err)
          }
        });
    }
    if (role === Roles.CUSTOMER)
      checkAut();
  }, [token, role])

  useEffect(() => {
    async function customerGetTickets() {
      await customerAPI.getTickets(token)
        .then(tickets => {
          console.log(tickets)
          setTickets(tickets)
        })
        .catch((err) => errorToast(err));
    }
    async function expertGetTickets() {
      await expertAPI.getTickets(token)
        .then(tickets => { setTickets(tickets) })
        .catch((err) => errorToast(err));
    }
    async function managerGetTickets() {
      await managerAPI.getTickets(token)
        .then(tickets => { setTickets(tickets) })
        .catch((err) => errorToast(err));
    }

    switch (role) {
      case Roles.CUSTOMER:
        customerGetTickets();
        break;
      case Roles.EXPERT:
        expertGetTickets();
        break;
      case Roles.MANAGER:
        managerGetTickets();
        break;
      default:
        break;
    }
    setFlag(false)

  }, [loggedIn, token, role, flag])

  useEffect(() => {
    const getProducts = async () => {
      await customerAPI.getProducts(token)
        .then(products => {
          setProducts(() => products);
        })
        .catch((err) => errorToast(err));
    }
    if (role === Roles.CUSTOMER)
      getProducts();
  }, [loggedIn, token, role])


  const closeTicket = async (ticketId) => {
    ticketId = Number.parseInt(ticketId)
    await customerAPI.compileSurvey(token, ticketId)
      .then(() => setTickets((prev) => prev.filter((ticket) => ticket.ticketId !== ticketId)))
      .catch((err) => errorToast(err))
  }

  const getMessages = async (ticketId) => {
    return await customerAPI.getMessages(token, ticketId)
      .then((messages) => { return messages })
      .catch((err) => errorToast(err));
  }

  const sendMessage = async (ticketId, message) => {
    if (role === Roles.CUSTOMER) {
      await customerAPI.sendMessage(token, message, ticketId)
        .then(() => setFlag(true))
        .catch((err) => errorToast(err));
    }
  }


  return (<><BrowserRouter>
    <AppNavBar loggedIn={loggedIn} role={role} logout={handleLogout} />
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
      <Route path="/editUser" element={loggedIn ? <EditUserPage user={user} handleEdit={handleEditProfile}/> : <Navigate to={"/"} />} />
      <Route path="/ticket/:ticketId" element={loggedIn ?
        <TicketPage getTicket={getTicket} closeTicket={closeTicket} getMessages={getMessages} sendMessage={ sendMessage} /> : <Navigate to={"/"} />} />
      <Route path="*" element={<h1 >Not Found</h1>} />
    </Routes>
  </BrowserRouter>
    <ToastContainer />
  </>
  );
}

export default App;
