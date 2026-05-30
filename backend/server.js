const express = require('express');
const mysql = require('mysql');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();

// Middleware
app.use(cors());
app.use(bodyParser.json());

// MySQL Connection
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'library_management'
});

connection.connect((err) => {
  if (err) {
    console.log('Error connecting to database:', err);
    return;
  }
  console.log('Connected to MySQL database');
});

// GET all books
app.get('/api/books', (req, res) => {
  const query = 'SELECT * FROM books';
  
  connection.query(query, (err, results) => {
    if (err) {
      console.log('Error fetching books:', err);
      res.status(500).json({ error: 'Failed to fetch books' });
      return;
    }
    res.json(results);
  });
});

// GET single book by ID
app.get('/api/books/:id', (req, res) => {
  const bookId = req.params.id;
  const query = 'SELECT * FROM books WHERE id = ?';
  
  connection.query(query, [bookId], (err, results) => {
    if (err) {
      console.log('Error fetching book:', err);
      res.status(500).json({ error: 'Failed to fetch book' });
      return;
    }
    if (results.length === 0) {
      res.status(404).json({ error: 'Book not found' });
      return;
    }
    res.json(results[0]);
  });
});

// POST - Add new book
app.post('/api/books', (req, res) => {
  const { title, author, quantity } = req.body;
  
  if (!title || !author || !quantity) {
    res.status(400).json({ error: 'Missing required fields' });
    return;
  }
  
  const query = 'INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)';
  
  connection.query(query, [title, author, quantity], (err, results) => {
    if (err) {
      console.log('Error adding book:', err);
      res.status(500).json({ error: 'Failed to add book' });
      return;
    }
    res.json({ id: results.insertId, title, author, quantity });
  });
});

// PUT - Update book
app.put('/api/books/:id', (req, res) => {
  const bookId = req.params.id;
  const { title, author, quantity } = req.body;
  
  const query = 'UPDATE books SET title = ?, author = ?, quantity = ? WHERE id = ?';
  
  connection.query(query, [title, author, quantity, bookId], (err, results) => {
    if (err) {
      console.log('Error updating book:', err);
      res.status(500).json({ error: 'Failed to update book' });
      return;
    }
    res.json({ id: bookId, title, author, quantity });
  });
});

// DELETE - Delete book
app.delete('/api/books/:id', (req, res) => {
  const bookId = req.params.id;
  const query = 'DELETE FROM books WHERE id = ?';
  
  connection.query(query, [bookId], (err, results) => {
    if (err) {
      console.log('Error deleting book:', err);
      res.status(500).json({ error: 'Failed to delete book' });
      return;
    }
    res.json({ message: 'Book deleted successfully' });
  });
});

// Start server
const PORT = 8080;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
  console.log(`API available at http://localhost:${PORT}/api/books`);
});
