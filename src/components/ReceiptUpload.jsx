import React, { useState } from 'react';
import axios from 'axios';

const ReceiptUpload = () => {
    const [file, setFile] = useState(null);
    const [items, setItems] = useState([]);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUpload = async () => {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('/api/receipts/upload', formData);
            setItems(response.data);
        } catch (error) {
            console.error('Upload failed:', error);
        }
    };

    const handleConfirm = async () => {
        try {
            await axios.post('/api/receipts/confirm', items, {
                params: { userId: 'current-user-id' }
            });
            setItems([]);
        } catch (error) {
            console.error('Confirmation failed:', error);
        }
    };

    return (
        <div>
            <input type="file" onChange={handleFileChange} />
            <button onClick={handleUpload}>Upload Receipt</button>
            
            {items.length > 0 && (
                <div>
                    <h3>Extracted Items:</h3>
                    <ul>
                        {items.map((item, index) => (
                            <li key={index}>{item}</li>
                        ))}
                    </ul>
                    <button onClick={handleConfirm}>Confirm Items</button>
                </div>
            )}
        </div>
    );
};

export default ReceiptUpload; 