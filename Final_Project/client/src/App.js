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
import TicketState from './model/ticketState';

function App() {

  const [user, setUser] = useState(null);
  const [loggedIn, setLoggedIn] = useState(false);
  const [token, setToken] = useState(null);
  const [tickets, setTickets] = useState([]);
  const [products, setProducts] = useState([]);
  const [role, setRole] = useState(null)
  const [experts, setExperts] = useState(null)
  const [dirty, setDirty] = useState(false)


  const handleLogin = async (credentials) => {
    await authAPI.login(credentials)
      .then((data) => {
        var decoded = jwt_decode(data);
        const newRole = decoded.resource_access["ticketing-service-client"].roles[0]
        setRole(newRole)
        setToken(data);
        setLoggedIn(true);
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
        return customerAPI.getTicket(token, ticketId)
          .catch((err) => errorToast(err));
      case Roles.EXPERT:
        return expertAPI.getTicket(token, ticketId)
          .catch((err) => errorToast(err));
      case Roles.MANAGER:
        return managerAPI.getTicket(token, ticketId)
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
        setDirty(true)
      })
  };

  const handleEditProfile = async (profile) => {
    await customerAPI.patchProfile(token, profile)
      .then(() => {
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
  }, [token, role, dirty])

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
    async function managerGetProducts() {
      await managerAPI.getProducts(token)
        .then(products => { setProducts(products) })
        .catch((err) => errorToast(err));
    }
    async function managerGetExperts() {
      await managerAPI.getExperts(token)
        .then(experts => { setExperts(experts) })
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
        managerGetProducts();
        managerGetExperts();
        break;
      default:
        break;
    }
    setDirty(false)

  }, [loggedIn, token, role, dirty])

  const customerCloseTicket = async (ticketId) => {
    await customerAPI.compileSurvey(token, ticketId)
      .then(() => {
        setTickets((prev) => prev.filter((ticket) => ticket.ticketId !== ticketId))
        setDirty(true)
      })
      .catch((err) => errorToast(err))
  }

  const customerReopenTicket = async (ticketId) => {
    await customerAPI.reopenTicket(token, ticketId)
      .then(() => { setDirty(true) })
      .catch(err => errorToast(err))
  }

  const getMessages = async (ticketId) => {

    switch (role) {
      case Roles.CUSTOMER:
        return await customerAPI.getMessages(token, ticketId)
          .catch((err) => errorToast(err));
      case Roles.EXPERT:
        return await expertAPI.getMessages(token, ticketId)
          .catch((err) => errorToast(err));
      case Roles.MANAGER:
        return await managerAPI.getMessages(token, ticketId)
          .catch((err) => errorToast(err));
      default:
        errorToast("You are not allowed to see messages")
    }
  }

  const sendMessage = async (ticketId, message) => {
    console.log("Sending message", ticketId, message)
    switch (role) {
      case Roles.CUSTOMER:
        await customerAPI.sendMessage(token, message, ticketId)
          .then(() => setDirty(true))
          .catch((err) => errorToast(err));
        break;
      case Roles.EXPERT:
        await expertAPI.sendMessage(token, message, ticketId)
          .then(() => setDirty(true))
          .catch((err) => errorToast(err));
        break;
      default:
        errorToast("You are not allowed to send messages")
        break;
    }
  }

  const managerAssignExpert = async (ticketId, expertId) => {
    console.log("Assigning expert", ticketId, expertId)
    await managerAPI.assignTicket(token, ticketId, expertId)
      .then(() => setDirty(true))
      .catch((err) => errorToast(err));
  }

  const managerHandleCloseTicket = async (ticket) => {
    switch (ticket.ticketState) {
      case TicketState.OPEN:
        managerAPI.closeTicket(token, ticket.ticketId)
          .then(() => setDirty(true))
        break
      case TicketState.IN_PROGRESS:
        managerAPI.closeTicket(token, ticket.ticketId)
          .then(() => setDirty(true))
        break
      case TicketState.REOPENED:
        managerAPI.closeTicket(token, ticket.ticketId)
          .then(() => setDirty(true))
        break
      default:
        console.error('Invalid ticket state')
        throw new Error('Invalid ticket state')
    }
  }

  const managerRelieveExpert = async (ticketId) => {
    await managerAPI.relieveExpert(token, ticketId)
      .then(() => setDirty(true))
      .catch((err) => errorToast(err));
  }

  const expertResolveTicket = async (ticketId) => {
    await expertAPI.resolveTicket(token, ticketId)
      .then(() => setDirty(true))
      .catch((err) => errorToast(err));
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
    getProductByID: getProductByID,
    customerCloseTicket: customerCloseTicket,
    customerReopenTicket: customerReopenTicket,
    managerAssignExpert: managerAssignExpert,
    managerHandleCloseTicket: managerHandleCloseTicket,
    managerRelieveExpert: managerRelieveExpert,
    expertResolveTicket: expertResolveTicket,
  }

  const userValues = {
    user: user,
    loggedIn: loggedIn,
    role: role,
    products: products,
    tickets: tickets,
    experts: experts
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
            <Route path="/user" element={loggedIn ? <UserPage user={user} /> : <Navigate to={"/"} />} />
            <Route path="/editUser" element={loggedIn ? <EditUserPage user={user} handleEdit={handleEditProfile} /> : <Navigate to={"/"} />} />
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
