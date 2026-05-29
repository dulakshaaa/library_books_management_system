

public class Test {
    public static void main(String[] args){
        Book b = new Book();
        b.setTitle("spring boot");
        b.setAuthor("dulaskha");
        b.setQuantity(3);
        
        BookDAO d = new BookDAO();
        d.addBook(b);
    }
    
}
