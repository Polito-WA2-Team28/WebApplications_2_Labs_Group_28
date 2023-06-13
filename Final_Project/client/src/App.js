import { BrowserRouter, Routes, Route } from 'react-router-dom';
import React from 'react';
import { ToastContainer } from 'react-toastify';
import LandingPage from './pages/landingPage';
import RegisterPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import Dashboard from './pages/dashboard';
import MyNavBar from './components/navbar';

function App() {
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
          <Route path="/" element={<LandingPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/dashboard" element={<Dashboard/>} />
          <Route path="*" element={<h1 >Not Found</h1>} />
        </Routes>
      </BrowserRouter>
      <ToastContainer />
    </div>
  );
}

export default App;
