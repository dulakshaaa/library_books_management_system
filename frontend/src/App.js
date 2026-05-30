import React, { useState } from 'react';
import './App.css';
import BookList from './components/BookList';
import BookForm from './components/BookForm';

function App() {
  // This controls whether to show the form or not
  const [refreshList, setRefreshList] = useState(0);

  // This function runs when a book is added
  const handleBookAdded = () => {
    setRefreshList(refreshList + 1);  // Refresh the list
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>📚 Library Books Management System</h1>
        <p>Manage your book collection easily</p>
      </header>

      <main className="App-main">
        <BookForm onBookAdded={handleBookAdded} />
        <BookList key={refreshList} />
      </main>

      <footer className="App-footer">
        <p>&copy; 2024 Library Management System. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;
