import { Routes, Route, Navigate, BrowserRouter } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ToastContainer } from 'react-toastify';
import LandingPage from './pages/landingPage';
import RegisterPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import Dashboard from './pages/dashboard';
import AppNavBar from './components/AppNavBar';
import authAPI from './APIs/authAPI'
import customerAPI from './APIs/customerAPI';
import expertAPI from './APIs/expertAPI';
import managerAPI from './APIs/managerAPI';
import UserPage from './pages/userPage';
import jwt_decode from "jwt-decode";
import TicketPage from './pages/ticketPage';
import ProductPage from './pages/productPage';
import Roles from './model/rolesEnum';
import { successToast, errorToast } from './components/toastHandler';
import EditUserPage from './pages/editUserPage';
import { ActionContext, UserContext } from './Context';

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

  const getTicketByID = async (ticketId) => {
    switch (role) {
      case Roles.CUSTOMER:
        return await customerAPI.getTicket(token, ticketId)
          .then((ticket) => { return ticket })
          .catch((err) => errorToast(err));
      case Roles.EXPERT:
        return await expertAPI.getTicket(token, ticketId)
          .then((ticket) => { return ticket })
          .catch((err) => errorToast(err));
      case Roles.MANAGER:
        return await managerAPI.getTicket(token, ticketId)
          .then((ticket) => { return ticket })
          .catch((err) => errorToast(err));
      default:
        console.error("Error: No role found")
        break;
    }
  }

  const getProductByID = async (productId) => {
    switch (role) {
      case Roles.CUSTOMER:
        return await customerAPI.getProduct(token, productId)
          .then((product) => { return product })
          .catch((err) => errorToast(err));
      case Roles.EXPERT:
        return await expertAPI.getProduct(token, productId)
          .then((product) => { return product })
          .catch((err) => errorToast(err));
      case Roles.MANAGER:
        return await managerAPI.getProduct(token, productId)
          .then((product) => { return product })
          .catch((err) => errorToast(err));
      default:
        console.error("Error: No role found")
        break;
    }
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
          setTickets(tickets)
        })
        .catch((err) => errorToast(err));
    }
    const customerGetProducts = async () => {
      await customerAPI.getProducts(token)
        .then(products => {
          setProducts(products);
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
        customerGetProducts();
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

  const closeTicket = async (ticketId) => {
    ticketId = Number.parseInt(ticketId)
    await customerAPI.compileSurvey(token, ticketId)
      .then(() => setTickets((prev) => prev.filter((ticket) => ticket.ticketId !== ticketId)))
      .catch((err) => errorToast(err))
  }

  const getMessages = async (ticketId) => {
    switch (role) {
      case Roles.CUSTOMER:
        await customerAPI.getMessages(token, ticketId)
          .then((messages) => { return messages })
          .catch((err) => errorToast(err));
        break;
      case Roles.EXPERT:
        await expertAPI.getMessages(token, ticketId)
          .then((messages) => { return messages })
          .catch((err) => errorToast(err));
        break;
      case Roles.MANAGER:
        await managerAPI.getMessages(token, ticketId)
          .then((messages) => { return messages })
          .catch((err) => errorToast(err));
        break;
      default:
        errorToast("You are not allowed to see messages")
    }
  }

  const sendMessage = async (ticketId, message) => {
    switch (role) {
      case Roles.CUSTOMER:
        await customerAPI.sendMessage(token, message, ticketId)
          .then(() => setFlag(true))
          .catch((err) => errorToast(err));
        break;
      case Roles.EXPERT:
        await expertAPI.sendMessage(token, message, ticketId)
          .then(() => setFlag(true))
          .catch((err) => errorToast(err));
        break;
      default:
        errorToast("You are not allowed to send messages")
        break;
    }
  }

  const actions = {
    getMessages: getMessages,
    sendMessage: sendMessage,
    handleLogin: handleLogin,
    handleLogout: handleLogout,
    handleRegistration: handleRegistration,
    handleEditProfile: handleEditProfile,
    handleCreateTicket: handleCreateTicket,
    getTicketByID: getTicketByID,
    getProductByID: getProductByID
  }

  const userValues = {
    user: user,
    loggedIn: loggedIn,
    role: role,
    products: products,
    tickets: tickets
  }

  return (
    <ActionContext.Provider value={actions}>
      <UserContext.Provider value={userValues}>
        <BrowserRouter>
          <AppNavBar />
          <Routes>
            <Route path="/" element={loggedIn ? <Navigate to={"/dashboard"} /> : <LandingPage />} />
            <Route path="/register" element={loggedIn ? <Navigate to={"/dashboard"} /> : <RegisterPage />} />
            <Route path="/login" element={loggedIn ? <Navigate to={"/dashboard"} /> : <LoginPage />} />
            <Route path="/dashboard" element={loggedIn ? <Dashboard /> : <Navigate to={"/"} />} />
            <Route path="/user" element={loggedIn ? <UserPage /> : <Navigate to={"/"} />} />
            <Route path="/editUser" element={loggedIn ? <EditUserPage /> : <Navigate to={"/"} />} />
            <Route path="/ticket/:ticketId" element={loggedIn ? <TicketPage /> : <Navigate to={"/"} />} />
            <Route path="/product/:productId" element={loggedIn ? <ProductPage /> : <Navigate to={"/"} />} />

            <Route path="*" element={<h1 >Not Found</h1>} />
          </Routes>
        </BrowserRouter>
        <ToastContainer />
      </UserContext.Provider>
    </ActionContext.Provider>
  )
}

export default App;
