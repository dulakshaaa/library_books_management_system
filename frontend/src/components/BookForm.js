import React, { useState } from 'react';
import API from '../api';
import './BookForm.css';

function BookForm({ onBookAdded }) {
  // These store what the user types in the form
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [quantity, setquantity] = useState('');
  const [loading, setLoading] = useState(false);

  // Function that runs when user clicks "Add Book" button
  const handleSubmit = async (e) => {
    e.preventDefault();  // Stop page from refreshing

    try {
      setLoading(true);

      // Create a new book object
      const newBook = {
        title: title,
        author: author,
        quantity: quantity
      };

      // Send it to Java backend
      await API.post('/books', newBook);

      // Clear the form
      setTitle('');
      setAuthor('');
      setquantity('');

      // Tell parent component to refresh the list
      onBookAdded();

      alert('Book added successfully!');
      setLoading(false);
    } catch (err) {
      alert('Failed to add book');
      setLoading(false);
    }
  };

  return (
    <div className="book-form">
      <h2>➕ Add New Book</h2>
      <form onSubmit={handleSubmit}>
        
        <div className="form-group">
          <label>Title:</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Author:</label>
          <input
            type="text"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Quantity:</label>
          <input
            type="text"
            value={quantity}
            onChange={(e) => setquantity(e.target.value)}
            required
          />
        </div>
    
        

        <button type="submit" disabled={loading}>
          {loading ? 'Adding...' : 'Add Book'}
        </button>
      </form>
    </div>
  );
}

export default BookForm;
