import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Book {

    int id;
    String title;
    String author;
    boolean borrowed;
    String borrower;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    boolean isBorrowed () {
        return borrowed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public Book (){};

    public Book (String title, String author) {

        this.title = title;
        this.author = author;
        this.borrowed = false;
        this.borrower = null;

        try {
            String lms = "Library";
            String dorEmy17 = "fowedlung12";
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = String.format("INSERT INTO Book(title,author,status) VALUES ('%s','%s','%s')",title,author,"Available");
            int count  = st.executeUpdate(query);
            System.out.println("student added");
            st.close();
            con.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
