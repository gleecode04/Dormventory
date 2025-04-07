import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Inventory = ({ userId }) => {
    const [items, setItems] = useState([]);

    useEffect(() => {
        loadItems();
    }, []);

    const loadItems = async () => {
        try {
            const response = await axios.get('/api/items/my-items', {
                params: { userId }
            });
            setItems(response.data);
        } catch (error) {
            console.error('Failed to load items:', error);
        }
    };

    const handleDelete = async (itemId) => {
        try {
            await axios.delete(`/api/items/${itemId}`, {
                params: { userId }
            });
            loadItems();
        } catch (error) {
            console.error('Failed to delete item:', error);
        }
    };

    return (
        <div>
            <h2>My Inventory</h2>
            <ul>
                {items.map(item => (
                    <li key={item.id}>
                        {item.name}
                        <button onClick={() => handleDelete(item.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Inventory; 