import React, { useState, useEffect } from 'react';
import API from '../api';
import './BookDetail.css';

function BookDetail({ bookId, onClose }) {
  // Store the book details and edit mode
  const [book, setBook] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState('');
  const [editAuthor, setEditAuthor] = useState('');
  const [editQuantity, setEditQuantity] = useState('');
  const [loading, setLoading] = useState(true);

  // Fetch book details when page loads
  useEffect(() => {
    fetchBookDetail();
  }, [bookId]);

  // Get one book from Java
  const fetchBookDetail = async () => {
    try {
      const response = await API.get(`/books/${bookId}`);  // Ask for specific book
      setBook(response.data);
      setEditTitle(response.data.title);
      setEditAuthor(response.data.author);
      setEditQuantity(response.data.quantity);
      setLoading(false);
    } catch (err) {
      alert('Failed to load book');
      setLoading(false);
    }
  };

  // Update book details
  const handleUpdate = async () => {
    try {
      const updatedBook = {
        title: editTitle,
        author: editAuthor,
        quantity: editQuantity
      };
      await API.put(`/books/${bookId}`, updatedBook);
      setBook(updatedBook);
      setIsEditing(false);
      alert('Book updated successfully!');
    } catch (err) {
      alert('Failed to update book');
    }
  };

  if (loading) return <p>Loading...</p>;
  if (!book) return <p>Book not found</p>;

  return (
    <div className="book-detail">
      <button onClick={onClose}>✕ Close</button>

      {!isEditing ? (
        <div>
          <h2>{book.title}</h2>
          <p><strong>Author:</strong> {book.author}</p>
          <p><strong>Quantity:</strong> {book.quantity}</p>
          <button onClick={() => setIsEditing(true)}>✏️ Edit</button>
        </div>
      ) : (
        <div>
          <h2>Edit Book</h2>
          <input
            type="text"
            value={editTitle}
            onChange={(e) => setEditTitle(e.target.value)}
            placeholder="Title"
          />
          <input
            type="text"
            value={editAuthor}
            onChange={(e) => setEditAuthor(e.target.value)}
            placeholder="Author"
          />
          <input
            type="text"
            value={editQuantity}
            onChange={(e) => setEditQuantity(e.target.value)}
            placeholder="Quantity"
          />
          <button onClick={handleUpdate}>Save Changes</button>
          <button onClick={() => setIsEditing(false)}>Cancel</button>
        </div>
      )}
    </div>
  );
}

export default BookDetail;
