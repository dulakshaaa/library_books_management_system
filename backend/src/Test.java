

public class Test {
    public static void main(String[] args){
        Book b =  new Book();
        b.setTitle("java basics");
        b.setAuthor("Sandaruwan Bandara");
        b.setQuantity(2);

       
        BookDAO.addBook(b);
    }
    
}
