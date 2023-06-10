import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static   Scanner si  = new Scanner(System.in);
    static Scanner ss = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n\n");
        System.out.println("WELCOME TO THE LIBRARY\n______________________");
        System.out.println("LOGIN\n______________________");
        boolean isLoggedin = false;
        int login;
        String name,email,mat;
        while(!isLoggedin){
            System.out.println("Login as :\n1.Administrator\n2.Student\nExit");
            login = si.nextInt();
            if (login == 1){
                System.out.print("Enter your name: ");
                name = ss.nextLine();
                System.out.print("Enter your email: ");
                email = ss.nextLine();

                Admin admin = searchAdmin(name,email);
                if (admin == null) {
                    System.out.println("Wrong username or Email. TRY AGAIN");
                } else {
                    System.out.println( "welcome " + admin.name );
                    //implement the  Admin menu
                    admin.menu();
                }

            }

            else  if (login == 2) {
                System.out.print("Enter your name: ");
                name = ss.nextLine();
                System.out.print("Enter your email: ");
                email = ss.nextLine();
                System.out.print("Enter your matricule: ");
                mat = ss.nextLine();

                Student student = searchStudent(name,email,mat);
                if (student == null) {
                    System.out.println("Wrong username or Email TRY AGAIN");
                } else {
                    System.out.println(" Welcome " + student.name);
                    //implement the  Student menu
                    student.menu();
                }
            }

            else {
                isLoggedin = true;
            }
        }

    }
    public static Admin searchAdmin (String name,String Email) {
        boolean found = false;
        Admin b = new Admin();
        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","fowedlung12");
            Statement st = con.createStatement();
            String query = "select * from Admin";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                if (name.equals(set.getString(1)) && Email.equals(set.getString(2))) {
                    found = true;
                    b.setName(set.getString(1));
                    b.setEmail(set.getString(2));
                    con.close();
                    break;
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        if (!found)return null;
        else return b;
    }

    public static Student searchStudent (String name,String Email, String mat) {
        boolean found = false;
        Student b = new Student();
        try {
            String lms = "Library";
            String dorEmy17 = "fowedlung12";
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + lms,"root", dorEmy17);
            Statement st = con.createStatement();
            String query = "select * from Student";
            ResultSet set = st.executeQuery(query);
            while (set.next()) {
                if (name.equals(set.getString(2)) && Email.equals(set.getString(3)) && mat.equals(set.getString(1))) {
                    found = true;
                    b.setName(set.getString(2));
                    b.setEmail(set.getString(3));
                    b.setMat(set.getString(1));
                    con.close();
                    break;
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        if (!found)return null;
        else return b;
    }
}