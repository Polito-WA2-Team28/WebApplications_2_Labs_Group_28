import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react';
import { ToastContainer } from 'react-toastify';
import {LandingPage} from './pages/landingPage';
import {RegisterPage} from './pages/registerPage';
import {LoginPage} from './pages/loginPage';
import {Dashboard} from './pages/dashboard';
import {AppNavBar} from './components/AppNavBar';
import authAPI from './APIs/authAPI'

function App() {

  const [user, setUser] = useState(null);
	const [message, setMessage] = useState("");

  const handleLogin = async (username, password) => {
    console.log('login');
    await authAPI.login(username, password)
      .then((data) => {
        setUser(data);
      })
  };

  const handleRegistration = async (username, password, email) => {
    console.log('register');
    await authAPI.register(username, password, email)
      .then((data) => {
        setUser(data);
      })
  };



  return (<><BrowserRouter>
      <AppNavBar/>
        <Routes>
          <Route path="/" element={user ? <Navigate to={"/dashboard"}/> : <LandingPage />} />
          <Route path="/register" element={<RegisterPage handleRegistration={handleRegistration} />} />
          <Route path="/login" element={<LoginPage setMessage={setMessage} handleLogin={handleLogin}/>} />
          <Route path="/dashboard" element={user ? <Dashboard user={user} />: <Navigate to={"/"} />}/>
          <Route path="*" element={<h1 >Not Found</h1>} />
        </Routes>
      </BrowserRouter>
    <ToastContainer />
    </>
  );
}

export default App;
