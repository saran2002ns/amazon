
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import SignUp from './components/SignUp';
import ForgotPassword from './components/ForgotPassword';
import AmazonHome from './components/AmazonHome';
import Checkout from './components/Checkout';
import OrderConfirmation from './components/OrderConfirmation';
import ViewOrders from './components/ViewOrders';
import AuthGuard from './components/AuthGuard';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/amazon" element={
            <AuthGuard>
              <AmazonHome />
            </AuthGuard>
          } />
          <Route path="/checkout" element={
            <AuthGuard>
              <Checkout />
            </AuthGuard>
          } />
          <Route path="/order-confirmation/:orderId" element={
            <AuthGuard>
              <OrderConfirmation />
            </AuthGuard>
          } />
          <Route path="/orders" element={
            <AuthGuard>
              <ViewOrders />
            </AuthGuard>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;



