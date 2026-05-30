import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    public Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_management","root","");

            System.out.println("connection successfull");

        }catch(ClassNotFoundException e){
            System.out.println("class not found "+e);
            e.printStackTrace();
        }catch(SQLException e){
            System.out.println("SQL error "+e);
            e.printStackTrace();
        }

        return conn;
    }
    
}
