import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ToastContainer } from 'react-toastify';
import {LandingPage} from './pages/landingPage';
import {RegisterPage} from './pages/registerPage';
import {LoginPage} from './pages/loginPage';
import {Dashboard} from './pages/dashboard';
import {AppNavBar} from './components/AppNavBar';
import authAPI from './APIs/authAPI'
import { customerAPI } from './APIs/customerAPI';
import { UserPage } from './pages/userPage';
import jwt_decode from "jwt-decode";
import { productAPI } from './APIs/productAPI';

function App() {

  const [user, setUser] = useState(null);
  const [loggedIn, setLoggedIn] = useState(false);
  const [token, setToken] = useState(null);
  const [message, setMessage] = useState("");
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

  const handleRegistration = async (username, password, email) => {
    console.log('register');
    await authAPI.register(username, password, email)
      .then((data) => {
        setUser(data);
      })
  };

  const handleLogout = async () => {
    console.log('logout');
        setToken(null);
        setLoggedIn(false);
        setUser(null);
  };


  useEffect(() => {
    const checkAut = async () => {
      await customerAPI.getProfile(token)
        .then((user) => setUser(() => user))
        .catch((err) => {
          if (err !== "Not authenticated") {
            console.error("error in getProfile: ", err)
            setMessage('Loading failed', 'danger')
          }
        });
    }
    checkAut();
  }, [token])

  useEffect(() => {
    const getTickets = async () => {
      await customerAPI.getTickets(token)
        .then(tickets => {
          setTickets(() => tickets);
          setMessage('Loading complete', 'success')
        })
        .catch((err) => {
          console.error("error in getTickets:", err);
          setMessage('Loading failed!', 'danger');
        });
    }
    getTickets();
  }, [loggedIn, token])

  useEffect(() => {
    const getProducts = async () => {
      await productAPI.getProducts(token)
        .then(products => {
          console.log(products);
          setProducts(() => products);
          setMessage('Loading complete', 'success')
        })
        .catch((err) => {
          console.error("error in getTickets:", err);
          setMessage('Loading failed!', 'danger');
        });
    }
    if(role === "CUSTOMER")
      getProducts();
  }, [loggedIn, token, role])




  return (<><BrowserRouter>
    <AppNavBar loggedIn={loggedIn} user={user} logout={handleLogout} />
        <Routes>
          <Route path="/" element={user ? <Navigate to={"/dashboard"}/> : <LandingPage />} />
          <Route path="/register" element={<RegisterPage handleRegistration={handleRegistration} />} />
          <Route path="/login" element={<LoginPage setMessage={setMessage} handleLogin={handleLogin}/>} />
      <Route path="/dashboard" element={loggedIn ? <Dashboard user={user} tickets={tickets} products={products} role={role} /> : <Navigate to={"/"} />} />
          <Route path="/user" element={loggedIn ?  <UserPage user={user} /> : <Navigate to={"/"} />} />
          <Route path="*" element={<h1 >Not Found</h1>} />
        </Routes>
      </BrowserRouter>
    <ToastContainer />
    </>
  );
}

export default App;
