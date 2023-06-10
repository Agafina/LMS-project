import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User {
    Scanner sc =new Scanner(System.in);
    public Admin(){};
    public Admin (String name,String email) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = String.format("INSERT INTO Admin(name,email) VALUES ('%s','%s')",name,email);
            int count  = st.executeUpdate(query);
            System.out.println("Admin added sucessfully");
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void addStuent () {
        String name, mat ,email;
        System.out.println("Enter the name, matricule and email");
        System.out.print("name: ");
        name = sc.nextLine();
        System.out.println("");
        System.out.print("matricule: ");
        mat = sc.nextLine();
        System.out.println("");
        System.out.print("email: ");
        email = sc.nextLine();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = String.format("INSERT INTO Student(matricule,name,email) VALUES ('%s','%s','%s')",mat,name,email);
            int count  = st.executeUpdate(query);
            System.out.println("Student added sucessfully");
            con.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void addBook () {
        String title, author;
        System.out.println("Enter the title and author");
        System.out.print("title: ");
        title = sc.nextLine();
        System.out.print("author: ");
        author = sc.nextLine();


        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = String.format("INSERT INTO Book(title,author,status) VALUES ('%s','%s','%s')",title,author,"Available");
            int count  = st.executeUpdate(query);
            System.out.println("Book added sucessfully");
            st.close();
            con.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void removeStudent (String mat) {
        Student stu = searchStudent(mat);
        if (stu == null) System.out.println("Student isn't registered in the library");
        else{
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
                Statement st = con.createStatement();
                String query = String.format("DELETE FROM Student WHERE matricule = '%s'",mat);
                int count  = st.executeUpdate(query);
                st.close();
                con.close();
                System.out.println("Student removed");
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    void removeBook (String title) {
        Book b = searchBook(title);
        if (b == null) System.out.println("Student isn't registered in the library");
        else{
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
                Statement st = con.createStatement();
                String query = String.format("DELETE FROM Book WHERE title = '%s'",title);
                int count  = st.executeUpdate(query);
                st.close();
                con.close();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    void showBooks () {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Book";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                System.out.println("Book " +  set.getInt(1));
                System.out.println("Title : " + set.getString(2));
                System.out.println("Author : " + set.getString(3));
                    if (set.getString(5) == null) System.out.println("Status : Available" );
                    else if (set.getString(5).equals("Available")) {
                        System.out.println("Status : Available" );
                    } else {
                        System.out.println("Status : Borrowed" );
                    }
                }
            st.close();
            con.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    void viewStudents () {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", "root", "fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Student";
            ResultSet set = st.executeQuery(query);
            int index = 1;
            while (set.next()) {
                System.out.println("Student  " + index);
                System.out.println("Matricule: " + (set.getString(1)));
                System.out.println("Name: " + set.getString(2));
                Student stu = searchStudent(set.getString(1));
                ArrayList<Book> books = stu.hasToReturn(stu.mat);
                ArrayList<Book> books1 = stu.hasBorrowed(stu.mat);
                if (books1.size()>0){
                    System.out.println(set.getString(2)+ " has borrowed the following books");
                    for (int i = 0 ; i < books1.size(); i++ ) {
                        System.out.println("-"+books1.get(i).title);
                    }
                }
               if (books.size()>0){
                   System.out.println(set.getString(2)+ " has to return the following books");
                   for (int i = 0 ; i < books.size(); i++ ) {
                       System.out.println("-"+books.get(i).title);
                   }
               }
                System.out.println("");
               index ++;
            }
            st.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void menu () {
        Scanner sm = new Scanner(System.in);
        int choice;
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("1.Register a Student t\n2.Add a Book\n3.Remove a  Student\n4.Remove a Book\n5.Display Students");
            choice = sm.nextInt();
            if (choice == 1) {
                int ans =1;
                while(ans == 1){
                    this.addStuent();
                    System.out.println("add another student ? \n1.yes\n2.no");
                    ans = sm.nextInt();
                }
            }
            else if (choice == 2) {
                int ans =1;
                while(ans == 1){
                    this.addBook();
                    System.out.println("add another Book ? \n1.yes\n2.no");
                    ans = sm.nextInt();
                }
            }
            else if (choice == 3) {
                int ans =1;
                while(ans == 1){
                    System.out.println("Enter the student's matricule");
                    String mat = sc.nextLine();
                    this.removeStudent(mat);
                    System.out.println("remove another student ? \n1.yes\n2.no");
                    ans = sm.nextInt();
                    sm.nextLine();
                }
            }
            else if (choice == 4) {
                int ans =1;
                while(ans == 1){
                    System.out.println("Enter the book title");
                    String title = sc.nextLine();
                    this.removeBook(title);
                    System.out.println("remove another book ? \n1.yes\n2.no");
                    ans = sm.nextInt();
                    sm.nextLine();
                }
            }
            else if (choice == 5) {
                int ans =1;
                while(ans == 1){
                    this.viewStudents();
                    System.out.println("view ? \n1.yes\n2.no");
                    ans = sc.nextInt();
                    sc.nextLine();
                }
            }
            else {
                System.out.println("Back to menu\n1.yes\n2.no");
                int men = sm.nextInt();
                sm.nextLine();
                if (men != 1) isRunning = false;
            }
        }

    }
}
