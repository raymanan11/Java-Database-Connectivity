

import java.sql.*;
import java.util.Scanner;
import java.util.*;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown
 */
public class JDBCMain {
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are
    //strings, but that won't always be the case.
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";
/**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public static void showMenu() {
        System.out.println("1.) List All Writing Groups");
        System.out.println("2.) List Data for Specified Writing Group");
        System.out.println("3.) List All Publishers");
        System.out.println("4.) List Data for Specified Publisher");
        System.out.println("5.) List All Book Titles");
        System.out.println("6.) List Data for Specified Book Title");
        System.out.println("7.) Insert a new book");
        System.out.println("8.) Insert a new publisher and update all books");
        System.out.println("9.) Remove a specified book");
    }

    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
//        DBNAME = in.nextLine();
        DBNAME = "JDBC";
        System.out.print("Database user name: ");
//        USER = in.nextLine();
        USER = "username";
        System.out.print("Database password: ");
//        PASS = in.nextLine();
        PASS = "password";
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        
        String continues;
        String selection;
        String writingGroup;
        String publisherName;
        String bookTitle;
        String currentVariable;
        String pAddress;
        String pPhoneNumber;
        String pEmail;
        boolean found = false;
        boolean unique = false;
        String groupName;
        String bookTitles;
        int numRows;
        
        Connection conn = null; //initialize the connection
        Statement stmt = null;
        ResultSet rs = null;
        
        WritingGroup wg = new WritingGroup();
        Publishers publishers = new Publishers();
        Books books = new Books();
        Data data = new Data();
        
        
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            
            continues = "a";
            
            while (!continues.equals("q")) {
                showMenu();
                System.out.println();
                
                String sql;
                System.out.print("Choose your number selection: ");
                selection = in.nextLine();
                
                switch(selection) {
                    // List All WritingGroups
                    case("1"):
                        System.out.println();
                        stmt = conn.createStatement();
                        sql = "SELECT * FROM WritingGroup";
                        rs = stmt.executeQuery(sql);
                        
                        wg.showAllWritingGroupData(rs);
                        break;
                    // List all the data for a group specified by the user
                    case("2"):
                        System.out.println();
                        wg.getAndShowGroupNames(stmt, conn);
                        System.out.println();
                        
                        currentVariable = "GroupName";
                        // Ask user to enter writing group of choice and get subsequent info for request
                        System.out.print("Please enter a writing group you would like information for: ");
                        writingGroup = in.nextLine();
                        System.out.println();
                        data.getSpecificData(currentVariable, conn, writingGroup, rs);
                        
                        break;
                    // List all publishers    
                    case("3"):
                        System.out.println();
                        stmt = conn.createStatement();
                        publishers.getAndShowAllPublisherData(rs, stmt);
                        break;
                    // List all the data for a pubisher specified by the user.
                    case("4"):
                        System.out.println();
                        publishers.getAndShowPublisherNames(stmt, conn);
                        System.out.println();
                        
                        currentVariable = "PublisherName";
                        // Ask user to enter Publisher of choice and get subsequent info for request
                        System.out.print("Please enter a publishing you would like information for: ");
                        publisherName = in.nextLine();
                        System.out.println();
                        publishers.getSpecificPublisherData(currentVariable, conn, publisherName, rs);
                        break;    
                    // List all book titles
                    case("5"):
                        System.out.println();
                        stmt = conn.createStatement();
                        books.getAndShowAllBooksData(rs, stmt);
                        break;
                    // List all the data for a single book specified by the user.
                    case("6"):
                        // show group names from books sintead of writing groups
                        // Show all available WritingGroups for user to choose
                        System.out.println();
                        wg.getAndShowGroupNames(stmt, conn);
                        System.out.println();
                        
                        System.out.println();
                        // Show all available Publishers for user to choose
                        stmt = conn.createStatement();
                        books.getAndShowBookTitles(rs, stmt);
                        System.out.println();
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName FROM WritingGroup";
 
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Writing Group name: ");
                            groupName = in.nextLine();
                            found = wg.writingGroupFound(groupName, rs);
                        }
                        while(found == false);
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName, BookTitle FROM Books";

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing and corresponding Book Title: ");
                            bookTitles = in.nextLine();
                            unique = books.existingBook(groupName, bookTitles, rs);
                        }
                        while(unique == true);
                        
                        currentVariable = "BookTitle";
                        // Ask user to enter Publisher of choice and get subsequent info for request
                        data.getSpecificData(currentVariable, conn, bookTitles, rs);
                        
                        break;  
                    case("7"):
                       
                        wg.getAndShowGroupNames(stmt, conn);
                        System.out.println();
                        publishers.getAndShowPublisherNames(stmt, conn);
                        System.out.println();
                        stmt = conn.createStatement();
                        books.getAndShowBookTitles(rs, stmt);
                        
                        String publisher;
                        
                        // we want an existing GroupName bc a new book cannot be written by no one
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName FROM WritingGroup";
 
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Writing Group name: ");
                            groupName = in.nextLine();
                            found = wg.writingGroupFound(groupName, rs);
                        }
                        while(found == false);      
                       
//                        //  add in a new publisher
//                        stmt = conn.createStatement();
//                        sql = "SELECT PublisherName FROM Publishers"; 
     
//                        System.out.println();
//                        System.out.print("Please enter a new Publisher addy: ");
//                        pAddress = in.nextLine();
//                        
//                        System.out.println();
//                        System.out.print("Please enter a new Publisher phone number: ");
//                        pPhoneNumber = in.nextLine();
//                        
//                        System.out.println();
//                        System.out.print("Please enter a new Publisher email: ");
//                        pEmail = in.nextLine();
//                        
//                        publishers.insertIntoPublishers(conn, publisher, pAddress, pPhoneNumber, pEmail);
                        
                        // can be same WritingGroup name but not an existing book title name
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName, BookTitle FROM Books";

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter new Book Title: ");
                            bookTitles = in.nextLine();
                            unique = books.existingBook(groupName, bookTitles, rs);
                        }
                        while(unique == false);
                        
                        stmt = conn.createStatement();
                        sql = "SELECT BookTitle, PublisherName FROM Books";
                        
                        // Asks the user to input an existing publisher
                        // If title and publisher is already in the database, it will prompt user
                        // to add a publisher who is not mathcing
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter an existing publisher: ");
                            publisher = in.nextLine();
                            // at this point we know that groupName and bookTitle are unique
                            unique = books.matchingTitleAndPublisherName(bookTitles, publisher, rs, conn);
                        }
                        while(unique == false);
                        

                        // if groupName and bookTitle equals an interation of the loop then return true
                        // but if not return false
                        // method(groupName, bookTitle, rs);
                        
                        System.out.println();
                        System.out.print("Please enter the year book was published: ");
                        int yearPublished = Integer.parseInt(in.nextLine());
                        
                        System.out.println();
                        System.out.print("Please enter the number of pages this book has: ");
                        int numberPages = Integer.parseInt(in.nextLine());
                        
                        books.insertIntoBooksTable(conn, groupName, bookTitles, publisher, yearPublished, numberPages);
                        
                        books.getAndShowBookTitles(rs, stmt);

                        break;
                    case("8"):
                        
                        String newPublisher;
                        
                        stmt = conn.createStatement();
                        sql = "SELECT PublisherName FROM Publishers"; 
                        rs = stmt.executeQuery(sql);
                        
                        publishers.showPublisherNames(rs);
                        
                        // validate input to get an existing one

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter an existing Publisher name: ");
                            publisher = in.nextLine();
                            found = publishers.publisherFound(publisher, rs);
                        }
                        while(found == false);
                        
                        // validates user input so gets a publisher that doesn't exist so far
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter a new Publisher name: ");
                            newPublisher = in.nextLine();
                            found = publishers.publisherFound(newPublisher, rs);
                        }
                        while(found == true);
                        
                        System.out.println();
                        System.out.print("Please enter a new Publisher addy: ");
                        pAddress = in.nextLine();
                        
                        System.out.println();
                        System.out.print("Please enter a new Publisher phone number: ");
                        pPhoneNumber = in.nextLine();
                        
                        System.out.println();
                        System.out.print("Please enter a new Publisher email: ");
                        pEmail = in.nextLine();
                        
                        publishers.insertIntoPublishers(conn, newPublisher, pAddress, pPhoneNumber, pEmail);
                        
                        System.out.println();
                        System.out.println("Books with old publisher name before adding new publishers");
                        rs = publishers.getBooksForSpecificPublisher(conn, publisher);
                        
                        books.showSelectedBookData(rs);
                        
                        System.out.println("Replacing books");
                        publishers.updatePublisherName(conn, newPublisher, publisher);
                        
                        System.out.println();
                        System.out.println("Books with new publisher name after replaced the old publisher name");
                        System.out.println("New publisher: " + newPublisher);
                        rs = publishers.getBooksForSpecificPublisher(conn, newPublisher);
                        
                        books.showSelectedBookData(rs);
                        
                        System.out.println();
                        
                        publishers.removePublisher(conn, publisher);
                        
                        publishers.getAndShowPublisherNames(stmt, conn);    
 
                        break;
                    case("9"):
                        
//                        stmt = conn.createStatement();
                        System.out.println();
                        wg.getAndShowGroupNames(stmt, conn);
                        System.out.println();

                        // Show all available Publishers for user to choose
                        stmt = conn.createStatement();
                        books.getAndShowBookTitles(rs, stmt);
                        System.out.println();
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName FROM WritingGroup";
 
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Writing Group name: ");
                            groupName = in.nextLine();
                            System.out.println("GroupName: " + groupName);
                            found = wg.writingGroupFound(groupName, rs);
                        }
                        while(found == false);
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName, BookTitle FROM Books";

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing and corresponding Book Title: ");
                            bookTitles = in.nextLine();
                            unique = books.existingBook(groupName, bookTitles, rs);
                        }
                        while(unique == true);
                        
                        // Book table before deleting row
                        System.out.println("Books table before deleting user selected book");
                        stmt = conn.createStatement();
                        books.getAndShowAllBooksData(rs, stmt);
                        
                        books.removeFromBooks(conn, groupName, bookTitles);
                        
                        System.out.println();
                        System.out.println("Books table after deleting user selected book");
                        stmt = conn.createStatement();
                        rs = books.getAndShowAllBooksData(rs, stmt);
                        
                        break;
                    default:
                        System.out.println("Invalid selection");
                            
                }
                
                System.out.println();
                System.out.print("Press any key to continue or q to quit: ");
                continues = in.nextLine();
                System.out.println();
                // ask for continues input again at very end of loop
            }
//            //STEP 6: Clean-up environment
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            publishers.closePreparedStatement();
            books.closePreparedStatement();
            data.closePreparedStatement();
            
        } 
        catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } 
        catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } 
        finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main

}//end FirstExample}
