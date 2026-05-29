import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookDAO {

    public static boolean addBook(Book b ){

        try {
            Connection c = DBConnection.getConnection();
            System.out.println(c);
            String sql = "INSERT INTO books (title, author, quantity) VALUES (?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
             ps.setString(1, b.getTitle());
             ps.setString(2, b.getAuthor());
             ps.setInt(3, b.getQuantity());
             
             ps.executeUpdate();
             System.out.println("record addded successfully");
             return true;

            }
        catch( Exception e){
            e.printStackTrace();
            return false;
        }


    }

}
