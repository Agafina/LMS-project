import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public  class User {


    String name;

    String email;
    void setName (String name) {
        this.name = name;
    }
    void setEmail(String email) {
        this.email = email;
    }

    String getEmail() {
        return this.email;
    }

    String getName() {
        return name;
    }

    Book searchBook (String title) {
        boolean found = false;
        Book b = new Book();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Book";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                if (title.equals(set.getString(2))) {
                    found = true;
                    b.setId(set.getInt(1));
                    b.setTitle(set.getString(2));
                    b.setAuthor(set.getString(3));
                    b.setBorrower(set.getString(4));
                    if (set.getString(5) == null) b.setBorrowed(false);
                    else if (set.getString(5).equals("Available")) {
                        b.setBorrowed(false);
                    } else {
                        b.setBorrowed(true);
                    }
                    st.close();
                    con.close();
                    break;
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        if (!found){
            System.out.println("couldn't find this book");
            return null;
        } else return b;
    }

    Book searchBook (int id) {
        boolean found = false;
        Book b = new Book();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Book";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                if (id == (set.getInt(1))) {
                    found = true;
                    b.setId(set.getInt(1));
                    b.setTitle(set.getString(2));
                    b.setAuthor(set.getString(3));
                    b.setBorrower(set.getString(4));
                    if (set.getString(5) == null) b.setBorrowed(false);
                    else if (set.getString(5).equals("Available")) {
                        b.setBorrowed(false);
                    } else {
                        b.setBorrowed(true);
                    }
                    st.close();
                    con.close();
                    break;
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        if (!found){
            System.out.println("couldn't find this book");
            return null;
        } else return b;
    }

    Student searchStudent (String mat) {
        boolean found = false;
        Student b = new Student();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Student";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {

                if (mat.equals(set.getString(1))) {
                    found = true;
                    b.setMat(set.getString(1));
                    b.setName(set.getString(2));
                    b.setEmail(set.getString(3));
                    st.close();
                    con.close();
                    break;
                }
            }
            st.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (!found) {
            b = null;
            return b;
        } else return b;
    }


}
