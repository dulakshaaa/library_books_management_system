import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class BookDAO {
    private Connection c = null;

    public BookDAO() {
        DBConnection conn = new DBConnection();
        c = conn.getConnection();
    }

    public boolean addBook(Book b) {

        try {
            // Connection c = DBConnection.getConnection();
            // System.out.println(c);
            String sql = "INSERT INTO books (title, author, quantity) VALUES (?,?,?)";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setInt(3, b.getQuantity());

            ps.executeUpdate();
            System.out.println("record addded successfully");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        try {
            String sql = "SELECT * FROM BOOKS";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Book b = new Book();

                b.setId(rs.getInt("id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setQuantity(rs.getInt("quantity"));

                books.add(b);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;

    }

    public boolean updatebooks(Book b) {
        try {
            String sql = "UPDATE BOOKS SET TITLE = ?, AUTHOR = ?, QUANTITY = ? WHERE ID =?";
            PreparedStatement ps = this.c.prepareStatement(sql);

            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setInt(3, b.getQuantity());
            ps.setInt(4, b.getId());

            int rows = ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {

        try {
            String sql = "DELETE FROM BOOKS WHERE ID = ?";
            PreparedStatement ps = this.c.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

    public Book getbyid(int id) {
        
        try {
            Book b = new Book();
            String sql = "SELECT * FROM BOOKS WHERE ID = ?";
            PreparedStatement ps = this.c.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet s = ps.executeQuery();

            

                if (s.next()) {
                    b.setId(s.getInt("id"));
                    b.setTitle(s.getString("title"));
                    b.setAuthor(s.getString("author"));
                    b.setQuantity(s.getInt("quantity"));

                    

                }
                return b;

            

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching book by id");

        }
        

    }

}
