public class testgetbyid {

    public static void main(String[] args){
        Book b = new Book();
        BookDAO dao = new BookDAO();
        dao.getbyid(3);

        System.out.println(b.getId()+" "+b.getTitle()+" "+b.getAuthor()+" "+b.getQuantity());
    }
    
}
