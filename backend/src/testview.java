import java.util.List;

public class testview {

    public static void main(String[] args){

        BookDAO dao = new BookDAO();
        List<Book> books = dao.getAllBooks();

        for(Book b : books){
            System.out.println("\nID - "+b.getId()+"\n"+"Title - "+b.getTitle()+"\n"+"Author - "+b.getAuthor()+"\n"+"Quantity - "+b.getQuantity());
        }


    }
    
}
