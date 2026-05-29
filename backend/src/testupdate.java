public class testupdate {

    public static void main(String[] args){
        Book b= new Book();
        b.setId(1);
        b.setTitle("new anthoneys");
        b.setAuthor("menal");
        b.setQuantity(10);

        BookDAO dao = new BookDAO();
        dao.updatebooks(b);
    }
    
}
