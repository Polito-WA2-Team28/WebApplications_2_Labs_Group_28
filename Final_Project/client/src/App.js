import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import React, { useState } from 'react';
import { ToastContainer } from 'react-toastify';
import LandingPage from './pages/landingPage';
import RegisterPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import Dashboard from './pages/dashboard';
import MyNavBar from './components/navbar';
import authAPI from './APIs/authAPI'

function App() {

  const [user, setUser] = useState(null);

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



  return (
    <div
      style={{
        scrollBehavior: 'smooth',
        //backgroundColor: '#e9ecef',
        height: '100vh',
        width: '100vw',
      }}
    >
      <BrowserRouter>
      <MyNavBar/>
        <Routes>
          <Route path="/" element={user ? <Navigate to={"/dashboard"}/> : <LandingPage />} />
          <Route path="/register" element={<RegisterPage handleRegistration={handleRegistration} />} />
          <Route path="/login" element={<LoginPage handleLogin={handleLogin}/>} />
          <Route path="/dashboard" element={<Dashboard user={user} />} />
          <Route path="*" element={<h1 >Not Found</h1>} />
        </Routes>
      </BrowserRouter>
      <ToastContainer />
    </div>
  );
}

export default App;
