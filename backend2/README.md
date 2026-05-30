# Running the Java REST API Backend

## Instructions

### Step 1: Compile and Run Java Backend (backend2)

**Option A: Using batch file (Windows)**
```
cd backend2
run.bat
```

**Option B: Manual compilation**
```
cd backend2
javac -cp "lib/*" src/*.java
java -cp "lib/*;src" RestAPI
```

Expected output:
```
Java REST API Server running on http://localhost:8080
API available at http://localhost:8080/api/books
```

### Step 2: Start React Frontend

Open another terminal:
```
cd frontend
npm start
```

The React app will run on http://localhost:3001 (or next available port)

## Architecture

```
React Frontend (localhost:3001)
    ↓
Java REST API (RestAPI.java on localhost:8080)
    ↓
BookDAO (handles database operations)
    ↓
MySQL Database
```

## API Endpoints

- **GET /api/books** - Get all books
- **GET /api/books/:id** - Get single book
- **POST /api/books** - Add new book
- **PUT /api/books/:id** - Update book
- **DELETE /api/books/:id** - Delete book

## What Each File Does

- **RestAPI.java** - REST API server using Java's built-in HttpServer
- **BookDAO.java** - Database operations (unchanged)
- **Book.java** - Book model (unchanged)
- **DBConnection.java** - MySQL connection (unchanged)
- **run.bat** - Batch file to compile and run

## CORS

CORS is enabled so React can access the Java API from a different port.
