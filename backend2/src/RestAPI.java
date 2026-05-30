import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class RestAPI {
    private static BookDAO bookDAO;

    public static void main(String[] args) throws IOException {
        bookDAO = new BookDAO();

        // Create HTTP server on port 8081
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Create context for CORS and routes
        server.createContext("/api/books", new BooksHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Java REST API Server running on http://localhost:8081");
        System.out.println("API available at http://localhost:8081/api/books");
    }

    static class BooksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Enable CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            // Handle preflight requests
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                if (method.equals("GET")) {
                    handleGet(exchange, path);
                } else if (method.equals("POST")) {
                    handlePost(exchange);
                } else if (method.equals("PUT")) {
                    handlePut(exchange, path);
                } else if (method.equals("DELETE")) {
                    handleDelete(exchange, path);
                } else {
                    sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
            }
        }

        private void handleGet(HttpExchange exchange, String path) throws IOException {
            if (path.equals("/api/books")) {
                // GET all books
                List<Book> books = bookDAO.getAllBooks();
                StringBuilder json = new StringBuilder("[");

                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    json.append("{");
                    json.append("\"id\":").append(book.getId()).append(",");
                    json.append("\"title\":\"").append(escapeJson(book.getTitle())).append("\",");
                    json.append("\"author\":\"").append(escapeJson(book.getAuthor())).append("\",");
                    json.append("\"quantity\":").append(book.getQuantity());
                    json.append("}");
                    if (i < books.size() - 1)
                        json.append(",");
                }

                json.append("]");
                sendResponse(exchange, 200, json.toString());
            } else if (path.startsWith("/api/books/")) {
                // GET single book by ID
                String idStr = path.substring("/api/books/".length());
                int id = Integer.parseInt(idStr);

                Book book = bookDAO.getbyid(id);
                if (book != null && book.getId() != 0) {
                    StringBuilder json = new StringBuilder("{");
                    json.append("\"id\":").append(book.getId()).append(",");
                    json.append("\"title\":\"").append(escapeJson(book.getTitle())).append("\",");
                    json.append("\"author\":\"").append(escapeJson(book.getAuthor())).append("\",");
                    json.append("\"quantity\":").append(book.getQuantity());
                    json.append("}");
                    sendResponse(exchange, 200, json.toString());
                } else {
                    sendResponse(exchange, 404, "{\"error\": \"Book not found\"}");
                }
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            // POST - Add new book
            String body = readBody(exchange);

            // Parse JSON manually
            String title = extractJsonValue(body, "title");
            String author = extractJsonValue(body, "author");
            int quantity = Integer.parseInt(extractJsonValue(body, "quantity"));

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);

            boolean success = bookDAO.addBook(book);

            if (success) {
                sendResponse(exchange, 201, "{\"message\": \"Book added successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\": \"Failed to add book\"}");
            }
        }

        private void handlePut(HttpExchange exchange, String path) throws IOException {
            // PUT - Update book
            String idStr = path.substring("/api/books/".length());
            int id = Integer.parseInt(idStr);

            String body = readBody(exchange);

            // Parse JSON manually
            String title = extractJsonValue(body, "title");
            String author = extractJsonValue(body, "author");
            int quantity = Integer.parseInt(extractJsonValue(body, "quantity"));

            Book book = new Book();
            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);

            boolean success = bookDAO.updatebooks(book);

            if (success) {
                sendResponse(exchange, 200, "{\"message\": \"Book updated successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\": \"Failed to update book\"}");
            }
        }

        private void handleDelete(HttpExchange exchange, String path) throws IOException {
            // DELETE - Delete book
            String idStr = path.substring("/api/books/".length());
            int id = Integer.parseInt(idStr);

            boolean success = bookDAO.delete(id);

            if (success) {
                sendResponse(exchange, 200, "{\"message\": \"Book deleted successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\": \"Failed to delete book\"}");
            }
        }

        private String readBody(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        }

        private String extractJsonValue(String json, String key) {
            String pattern = "\"" + key + "\":\"";
            int start = json.indexOf(pattern);
            if (start == -1) {
                // Try without quotes (for numbers)
                pattern = "\"" + key + "\":";
                start = json.indexOf(pattern);
                if (start == -1)
                    return "";
                start += pattern.length();
                int end = json.indexOf(",", start);
                if (end == -1)
                    end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
            start += pattern.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }

        private String escapeJson(String str) {
            if (str == null)
                return "";
            return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
        }

        private void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
            byte[] bytes = response.getBytes();
            exchange.sendResponseHeaders(code, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
