import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Home = ({ userId }) => {
  const [recentItems, setRecentItems] = useState([]);
  const [recentRequests, setRecentRequests] = useState([]);

  useEffect(() => {
    loadRecentData();
  }, []);

  const loadRecentData = async () => {
    try {
      const [itemsRes, requestsRes] = await Promise.all([
        axios.get('/api/items/room-items', { params: { userId } }),
        axios.get('/api/requests', { params: { userId } })
      ]);
      
      setRecentItems(itemsRes.data.slice(0, 5));
      setRecentRequests(requestsRes.data.slice(0, 5));
    } catch (error) {
      console.error('Failed to load recent data:', error);
    }
  };

  return (
    <div className="row">
      <div className="col-md-6">
        <h2>Recent Room Items</h2>
        <ul className="list-group">
          {recentItems.map(item => (
            <li key={item.id} className="list-group-item">
              {item.name} - {item.owner.name}
            </li>
          ))}
        </ul>
      </div>
      
      <div className="col-md-6">
        <h2>Recent Requests</h2>
        <ul className="list-group">
          {recentRequests.map(request => (
            <li key={request.id} className="list-group-item">
              {request.content} - {request.sender.name}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Home; 