import React, { useState, useEffect } from 'react';
import API from '../api';
import './BookList.css';

function BookList() {
  // These are like containers to store data
  const [books, setBooks] = useState([]);  // List of all books
  const [loading, setLoading] = useState(true);  // Is it loading?
  const [error, setError] = useState(null);  // Any errors?

  // This runs when the page first loads
  useEffect(() => {
    fetchBooks();
  }, []);

  // Function to get all books from Java backend
  const fetchBooks = async () => {
    try {
      setLoading(true);
      const response = await API.get('/books');  // Ask Java for all books
      setBooks(response.data);  // Store the books
      setLoading(false);
    } catch (err) {
      setError('Failed to load books');
      setLoading(false);
    }
  };

  // Function to delete a book
  const deleteBook = async (bookId) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      try {
        await API.delete(`/books/${bookId}`);  // Tell Java to delete
        fetchBooks();  // Refresh the list
      } catch (err) {
        alert('Failed to delete book');
      }
    }
  };

  // Show loading message
  if (loading) return <p>Loading books...</p>;

  // Show error message
  if (error) return <p>{error}</p>;

  // Show the table of books
  return (
    <div className="book-list">
      <h2>📚 All Books</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Author</th>
            <th>Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <tr key={book.id}>
              <td>{book.id}</td>
              <td>{book.title}</td>
              <td>{book.author}</td>
              <td>{book.quantity}</td>
              <td>
                <button onClick={() => deleteBook(book.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default BookList;
