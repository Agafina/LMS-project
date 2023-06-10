import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Student extends User{

    Scanner si = new Scanner(System.in);
    Scanner sc = new Scanner(System.in);
    String mat;
    void setMat(String mat) {
        this.mat = mat;
    }

    String getMat () {
        return mat;
    }

    public Student(){};
    public Student (String name, String email, String mat) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = String.format("INSERT INTO Student(matricule,name,email) VALUES ('%s','%s','%s')",mat,name,email);
            int count  = st.executeUpdate(query);
            System.out.println("student added");
            st.close();
            con.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    void borrowBook (String title) {

        if (this.hasToReturn(this.mat).size() == 0) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime returnDate = currentDateTime.plusDays(5);
            // Define the desired date and time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // Format the current date and time as a string
            String borrow_date = currentDateTime.format(formatter);
            String return_date = returnDate.format(formatter);

            Book b = searchBook(title);
            if (b != null && !b.isBorrowed()) {
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "fowedlung12");
                    Statement st = con.createStatement();
                    String query = String.format("UPDATE Book set status = '%s',borrower_mat = '%s' where title = '%s'", "Borrowed", this.mat, b.title);
                    String query2 = String.format("INSERT INTO BorrowingLog (book_id,stu_mat,borrow_date,return_date) VALUES (%d,'%s','%s','%s')", b.id, this.mat, borrow_date, return_date);
                    int count = st.executeUpdate(query);
                    int count2 = st.executeUpdate(query2);
                    System.out.println(b.title + " has been borrowed");
                    st.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else if (b.isBorrowed()) {
                System.out.println("This book is presently borrowed do you want to reserve it instead?");
                int choice;
                boolean running = true;
                while (running) {
                    System.out.println("1.Reserve\n2.leave");
                    choice = si.nextInt();
                    if (choice == 1) {
                        this.reserve_book(title);
                        break;
                    } else if (choice == 2) {
                        running = false;
                        break;
                    } else System.out.println("choose either 1 or 2");
                }
            }
        }
        else {
            System.out.println("you have to return all the books you borrowed");
                System.out.println("you also have to pay a fine of " + calculateFine(this.hasToReturn(this.mat)) + " for the " + this.hasToReturn(this.mat).size() + " books you owe");

        }
    }

    void return_book (String title) {
        Book b = searchBook(title);
        if (b != null && b.borrower.equals(this.mat)) {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
                Statement st = con.createStatement();
                String query = String.format("UPDATE Book set status = '%s',borrower_mat = '%s' where title = '%s'","Available",null,b.title);
                String query2 = String.format("DELETE FROM BorrowingLog WHERE book_id = %d", b.id);
                int count  = st.executeUpdate(query);
                int count2 = st.executeUpdate(query2);
                String query3 = "SELECT * FROM ReservationQueue";
                ResultSet set = st.executeQuery(query3);
                while(set.next()){
                    if (b.id == set.getInt(2)){
                        Student nextBorrower = searchStudent(set.getString(3));
                         nextBorrower.borrowBook(b.title);
                        String query5 = String.format("DELETE FROM ReservationQueue WHERE id = %d", set.getInt(1));
                        st.executeUpdate(query5);
                        break;
                    }
                }
                st.close();
                con.close();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    void reserve_book(String title) {

        if (this.hasToReturn(this.mat).size() > 0) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String borrow_date = currentDateTime.format(formatter);

            Book b = searchBook(title);
            if (b != null && !b.isBorrowed()) {
                System.out.println("This book is not borrowed at the moment. Borrow now instead? ");
                int choice;
                boolean running = true;
                while (running) {
                    System.out.println("1.Borrow\n2.leave");
                    choice = si.nextInt();
                    if (choice == 1) {
                        this.borrowBook(title);
                        break;
                    } else if (choice == 2) {
                        running = false;
                        break;
                    } else System.out.println("choose either 1 or 2");
                }
            } else if (b != null) {
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "fowedlung12");
                    Statement st = con.createStatement();
                    String query = String.format("INSERT INTO ReservationQueue(book_id,stu_mat,reserve_date) VALUES ('%d','%s','%s')", b.id, this.mat, borrow_date);
                    int count = st.executeUpdate(query);
                    System.out.println("you reserved the book : " + b.title);
                    st.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else {
            System.out.println("you have to return all the books you borrowed");
            if ( this.hasToReturn(this.mat).size() > 2 ){
                System.out.println("you also have to pay a fine of " + calculateFine(this.hasToReturn(this.mat)) + " for the " + this.hasToReturn(this.mat).size() + " books you owe");
            }
        }
    }

    void viewBooks () {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Book";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                System.out.println("Book " + (set.getInt(1)));
                System.out.println("Title: " + (set.getString(2)));
                System.out.println("Author: " + (set.getString(3)));
                if (set.getString(5).equals("Available")) {
                    System.out.println("Status: Available");
                } else {
                    System.out.println("Status: Borrowed");
                }
            }
            st.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    int  calculateFine (ArrayList<Book> boolList){
        int numOfBooks = boolList.size();
        return numOfBooks*500;
    }

    public ArrayList<Book> hasToReturn(String mat) {
        ArrayList<Book> returnList = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "fowedlung12");
            Statement st = con.createStatement();
            String query = "SELECT *, DATE_FORMAT(return_date, '%Y-%m-%d %H:%i:%s') AS formatted_date FROM borrowinglog;";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                if (mat.equals(set.getString(3))) {
                    String return_date = set.getString(6);
                    if (return_date != null) {  // Add null check
                        LocalDateTime returnDate = LocalDateTime.parse(return_date, formatter);
                        if (returnDate.isBefore(currentDateTime)) {
                            Book e = searchBook(set.getInt(2));
                            if (e != null) {
                                returnList.add(e);
                            }
                        }
                    }
                }
            }
            st.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return returnList;
    }

    public ArrayList<Book> hasBorrowed (String mat) {
        ArrayList<Book> returnList = new ArrayList<Book>();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = "SELECT *  FROM borrowinglog;";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                if (mat.equals(set.getString(3))) {
                        Book e = searchBook(set.getInt(2));
                        if (e != null) returnList.add(e);
                }
            }
            st.close();
            con.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return returnList;
    }
    void menu () {
        int choice;
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("1.Lend a Book\n2.Return the book \n3.Reserve  q Book\n4.view books\n5.Logout");
            choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1) {
                int ans =1;
                while(ans == 1){
                    System.out.println("Enter the title of the book");
                    String title = sc.nextLine();
                    this.borrowBook(title);
                    System.out.println("borrow another book ? \n1.yes\n2.no");
                    ans = sc.nextInt();
                    sc.nextLine();
                }
            }
            else if (choice == 2) {
                int ans =1;
                while(ans == 1){
                    System.out.println("Enter the title of the book");
                    String title = sc.nextLine();
                    this.return_book(title);
                    System.out.println("return another book ? \n1.yes\n2.no");
                    ans = sc.nextInt();
                    sc.nextLine();
                }
            }
            else if (choice == 3) {
                int ans =1;
                while(ans == 1){
                    System.out.println("Enter the title of the book");
                    String title = sc.nextLine();
                    this.reserve_book(title);
                    System.out.println("reserve another book ? \n1.yes\n2.no");
                    ans = sc.nextInt();
                    sc.nextLine();
                }
            }
            else if (choice == 4) {
                int ans =1;
                while(ans == 1){
                    this.viewBooks();
                    System.out.println("view again ? \n1.yes\n2.no");
                    ans = sc.nextInt();
                    sc.nextLine();
                }
            }
            else {
                System.out.println("Logout\n1.yes\n2.no");
                int men = sc.nextInt();
                sc.nextLine();
                isRunning = men != 1;
            }
        }
    }
}
