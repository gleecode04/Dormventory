import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Inventory from './Inventory';
import ReceiptUpload from './ReceiptUpload';
import Requests from './Requests';
import Navigation from './Navigation';
import Home from './Home';

const App = () => {
  // In a real app, this would come from authentication
  const userId = "current-user-id";
  
  return (
    <Router>
      <div>
        <Navigation />
        
        <main className="container mt-4">
          <Routes>
            <Route path="/" element={<Home userId={userId} />} />
            <Route path="/inventory" element={<Inventory userId={userId} />} />
            <Route path="/upload" element={<ReceiptUpload userId={userId} />} />
            <Route path="/requests" element={<Requests userId={userId} />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App; 