import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Navigation = () => {
  const location = useLocation();
  
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">
        <Link className="navbar-brand" to="/">Dormventory</Link>
        <div className="navbar-nav">
          <Link 
            className={`nav-link ${location.pathname === '/' ? 'active' : ''}`} 
            to="/"
          >
            Home
          </Link>
          <Link 
            className={`nav-link ${location.pathname === '/inventory' ? 'active' : ''}`} 
            to="/inventory"
          >
            My Items
          </Link>
          <Link 
            className={`nav-link ${location.pathname === '/upload' ? 'active' : ''}`} 
            to="/upload"
          >
            Upload Receipt
          </Link>
          <Link 
            className={`nav-link ${location.pathname === '/requests' ? 'active' : ''}`} 
            to="/requests"
          >
            Requests
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navigation; 