

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
    
    public static boolean writingGroupFound(String writingGroup, ResultSet rs) throws SQLException {
        boolean foundWritingGroup = false;
        String groupName;
        while (rs.next()) {
            groupName = rs.getString("GroupName");
            if (groupName.equals(writingGroup)) {
                foundWritingGroup = true;
                break;
            }
        }
        return foundWritingGroup;
    }
    
    public static boolean publisherFound(String publisherz, ResultSet rs) throws SQLException {
        boolean foundPublisher = false;
        String publisher;
        while (rs.next()) {
            publisher = rs.getString("PublisherName");
            if (publisher.equals(publisherz)) {
                foundPublisher = true;
                break;
            }
        }
        return foundPublisher;
    }
    
    public static boolean existingBook(String groupName, String bookTitle, ResultSet rs) throws SQLException {
        boolean uniqueBook = true;
        String gName;
        String bTitle;
        while (rs.next()) {
            gName = rs.getString("GroupName");
            bTitle = rs.getString("BookTitle");
            if (gName.equals(groupName) && bTitle.equals(bookTitle)) {
                uniqueBook = false;
                break;
            }
        }
        return uniqueBook;
    }
    
    public static void showWritingGroupNames(ResultSet rs) throws SQLException {
        System.out.println("Available Writing Groups: ");
        System.out.println();
        while (rs.next()) {
            String groupName = rs.getString("GroupName");
            System.out.println(groupName);
        }
    }
    
    public static Statement showGroupNames(Statement stmt, Connection conn) throws SQLException {
        String sql;
        ResultSet rs;
        // Show all available Writing Groups for user to choose
        stmt = conn.createStatement();
        sql = "SELECT GroupName FROM WritingGroup";
        rs = stmt.executeQuery(sql);
        showWritingGroupNames(rs);
        return stmt;
    }
    
    public static Statement showGroupNamesFromBooks(Statement stmt, Connection conn) throws SQLException {
        String sql;
        ResultSet rs;
        // Show all available Writing Groups for user to choose
        stmt = conn.createStatement();
        sql = "SELECT GroupName FROM Books";
        rs = stmt.executeQuery(sql);
        showWritingGroupNames(rs);
        return stmt;
    }
    
    public static void showAllWritingGroupData(ResultSet rs) throws SQLException {
        while (rs.next()) {
            //Retrieve by column name
            String groupName = rs.getString("GroupName");
            String headWriter = rs.getString("HeadWriter");
            int yearFormed = rs.getInt("YearFormed");
            String subject = rs.getString("Subject");

            //Display values . 
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Head Writer: " + headWriter + "   ");
            System.out.print("Year Formed: " + yearFormed + "   ");
            System.out.print("Subject: " + subject);
            System.out.println();
        }
    }
    
    public static void showAllPublisherNames(ResultSet rs) throws SQLException {
        System.out.println("Existing Publishers that published a book: ");
        System.out.println();
        while (rs.next()) {
            //Retrieve by column name
            String publisherName = rs.getString("PublisherName");
            //Display values
            System.out.println(publisherName);
        }
    }
    
    public static void showAllPublishersData(ResultSet rs) throws SQLException {
        
        while (rs.next()) {
            //Retrieve by column name
            String publisherName = rs.getString("PublisherName");
            String publisherAddress = rs.getString("PublisherAddress");
            String publisherPhone = rs.getString("PublisherPhone");
            String publisherEmail = rs.getString("PublisherEmail");

            //Display values
            System.out.print("Publisher Name: " + publisherName + "   ");
            System.out.print("Publisher Address: " + publisherAddress + "   ");
            System.out.print("Publisher Phone: " + publisherPhone + "   ");
            System.out.print("Publisher Email: " + publisherEmail);
            System.out.println();
        }
    }
    
    public static void showPublisherNames(ResultSet rs) throws SQLException {
        System.out.println("Available Publishers: ");
        System.out.println();
        while (rs.next()) {
            String publisherName = rs.getString("PublisherName");
            System.out.println(publisherName);
        }
    }
    
    public static void showBookNames(ResultSet rs) throws SQLException {
        System.out.println("Available Books: ");
        System.out.println();
        while (rs.next()) {
            String bookTitles = rs.getString("BookTitle");
            System.out.println(bookTitles);
        }
    }
    
    public static ResultSet getAllBooksData(ResultSet rs, Statement stmt) throws SQLException {
        String sql;
        sql = "SELECT * FROM Books";
        rs = stmt.executeQuery(sql);
        showAllBooksData(rs);
        return rs;
    }
    
    public static void showAllBooksData(ResultSet rs) throws SQLException {
        while (rs.next()) {
            //Retrieve by column name
            String groupName = rs.getString("GroupName");
            String bookTitle = rs.getString("BookTitle");
            String publisherName = rs.getString("PublisherName");
            int yearPublished = rs.getInt("YearPublished");
            int numberPages = rs.getInt("NumberPages");

            //Display values
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Book Title: " + bookTitle + "   ");
            System.out.print("Publisher Name: " + publisherName + "   ");
            System.out.print("Year Published: " + yearPublished + "   ");
            System.out.print("Number of Pages: " + numberPages + "   ");
            System.out.println();
        }
    }
    
    public static void showSelectedBookData(ResultSet rs) throws SQLException {
        boolean correctInput = false;
        while(rs.next()) {
            correctInput = true;
            String groupName = rs.getString("GroupName");
            String publisherName = rs.getString("PublisherName");
            String bookTitle = rs.getString("BookTitle");
            int yearPublished = rs.getInt("YearPublished");
            int numberPages = rs.getInt("NumberPages");
            
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Publisher Name: " + publisherName + "   ");
            System.out.print("Book Title: " + bookTitle + "   ");
            System.out.print("Year Published: " + yearPublished + "   ");
            System.out.print("Number of Pages: " + numberPages + "   ");
            System.out.println();
        }
        
        if (!correctInput) {
            System.out.println("Unable to show Book data because of nonexistant publisher");
        }
    }
    
    public static ResultSet getBooksForSpecificPublisher(Connection conn, String publisher) throws SQLException {
        String sql;
        PreparedStatement pstmt;
        ResultSet rs;
        // show books of old publisher before adding new publishers
        sql = "SELECT * FROM Books "
                + "WHERE PublisherName = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, publisher);
        rs = pstmt.executeQuery();
        return rs;
    }
    
    public static ResultSet getSpecificData(String currentVariable, Connection conn, String writingGroup, ResultSet rs) throws SQLException {
        String sql;
        PreparedStatement pstmt;
        sql = "SELECT * FROM WritingGroup "
                + "LEFT OUTER JOIN Books using (GroupName) "
                + "LEFT OUTER JOIN Publishers using (PublisherName) "
                + "WHERE " + currentVariable + " = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, writingGroup);
        rs = pstmt.executeQuery();
        showSelectedData(rs);
        return rs;
    }
    
    public static void showSelectedData(ResultSet rs) throws SQLException {
        boolean correctInput = false;
        while (rs.next()) {
            correctInput = true;
            String groupName = rs.getString("GroupName");
            String headWriter = rs.getString("HeadWriter");
            int yearFormed = rs.getInt("YearFormed");
            String subject = rs.getString("Subject");

            //Display values
            System.out.println("WRITING GROUP");
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Head Writer: " + headWriter + "   ");
            System.out.print("Year Formed: " + yearFormed + "   ");
            System.out.print("Subject: " + subject);
            System.out.println();
            
            String publisherName = rs.getString("PublisherName");
            String publisherAddress = rs.getString("PublisherAddress");
            String publisherPhone = rs.getString("PublisherPhone");
            String publisherEmail = rs.getString("PublisherEmail");

            //Display values
            System.out.println("PUBLISHERS");
            System.out.print("Publisher Name: " + publisherName + "   ");
            System.out.print("Publisher Address: " + publisherAddress + "   ");
            System.out.print("Publisher Phone: " + publisherPhone + "   ");
            System.out.print("Publisher Email: " + publisherEmail);
            System.out.println();
            
            String bookTitle = rs.getString("BookTitle");
            int yearPublished = rs.getInt("YearPublished");
            int numberPages = rs.getInt("NumberPages");

            //Display values
            System.out.println("BOOKS");
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Publisher Name: " + publisherName + "   ");
            System.out.print("Book Title: " + bookTitle + "   ");
            System.out.print("Year Published: " + yearPublished + "   ");
            System.out.print("Number of Pages: " + numberPages + "   ");
            System.out.println();
        }
        
        if (!correctInput) {
            System.out.println("Wrong name given!");
        }
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
        boolean found = false;
        boolean unique = false;
        String groupName;
        String bookTitles;
        int numRows;
        
        Connection conn = null; //initialize the connection
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
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
                        
                        showAllWritingGroupData(rs);
                        break;
                    // List all the data for a group specified by the user
                    case("2"):
                        System.out.println();
                        showGroupNames(stmt, conn);
                        System.out.println();
                        
                        currentVariable = "GroupName";
                        // Ask user to enter writing group of choice and get subsequent info for request
                        System.out.print("Please enter a writing group you would like information for: ");
                        writingGroup = in.nextLine();
                        System.out.println();
                        rs = getSpecificData(currentVariable, conn, writingGroup, rs);
                        
                        break;
                    // List all publishers    
                    case("3"):
                        System.out.println();
                        stmt = conn.createStatement();
                        sql = "SELECT * FROM Publishers";
                        rs = stmt.executeQuery(sql);
                        
                        showAllPublishersData(rs);
                        break;
                    // List all the data for a pubisher specified by the user.
                    case("4"):
                        System.out.println();
                        // Show all available Publishers for user to choose
                        stmt = conn.createStatement();
                        sql = "SELECT PublisherName FROM Publishers";
                        rs = stmt.executeQuery(sql);
                        
                        showPublisherNames(rs);
                        System.out.println();
                        
                        currentVariable = "PublisherName";
                        // Ask user to enter Publisher of choice and get subsequent info for request
                        System.out.print("Please enter a publishing you would like information for: ");
                        publisherName = in.nextLine();
                        System.out.println();
                        rs = getSpecificData(currentVariable, conn, publisherName, rs);
                        break;    
                    // List all book titles
                    case("5"):
                        System.out.println();
                        stmt = conn.createStatement();
                        rs = getAllBooksData(rs, stmt);
                        break;
                    // List all the data for a single book specified by the user.
                    case("6"):
                        // show group names from books sintead of writing groups
                        // Show all available WritingGroups for user to choose
                        System.out.println();
                        showGroupNamesFromBooks(stmt, conn);
                        System.out.println();
                        
                        System.out.println();
                        // Show all available Publishers for user to choose
                        stmt = conn.createStatement();
                        sql = "SELECT Booktitle FROM Books";
                        rs = stmt.executeQuery(sql);
                        
                        showBookNames(rs);
                        System.out.println();
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName FROM WritingGroup";
 
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Writing Group name: ");
                            groupName = in.nextLine();
                            found = writingGroupFound(groupName, rs);
                        }
                        while(found == false);
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName, BookTitle FROM Books";

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing and corresponding Book Title: ");
                            bookTitles = in.nextLine();
                            unique = existingBook(groupName, bookTitles, rs);
                        }
                        while(unique == true);
                        
                        currentVariable = "BookTitle";
                        // Ask user to enter Publisher of choice and get subsequent info for request
                        rs = getSpecificData(currentVariable, conn, bookTitles, rs);
                        
                        break;  
                    case("7"):
                       
                        String publisher;
                        
                        // we want an existing GroupName bc a new book cannot be written by no one
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName FROM WritingGroup";
 
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Writing Group name: ");
                            groupName = in.nextLine();
                            System.out.println("GroupName: " + groupName);
                            found = writingGroupFound(groupName, rs);
                        }
                        while(found == false);      
                        
                        //  thought we wanted an existing Publisher but can't have duplicates :(
                        stmt = conn.createStatement();
                        sql = "SELECT PublisherName FROM Publishers"; 
                        
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter new Publisher name: ");
                            publisher = in.nextLine();
                            found = publisherFound(publisher, rs);
                        }
                        while(found == false);
                        
                        // can be same WritingGroup name but not an existing book title name
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName, BookTitle FROM Books";

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter new Book Title: ");
                            bookTitles = in.nextLine();
                            unique = existingBook(groupName, bookTitles, rs);
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
                        
                        sql = "INSERT INTO Books (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) VALUES (?,?,?,?,?)";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.clearParameters();
                        pstmt.setString(1, groupName);
                        pstmt.setString(2, bookTitles);
                        pstmt.setString(3, null);
                        pstmt.setInt(4, yearPublished);
                        pstmt.setInt(5, numberPages);
                        
                        numRows = pstmt.executeUpdate();
                        System.out.println("Number of Rows affected:  + numRows");
                        
                        
                        break;
                    case("8"):
                        
                        String pAddress;
                        String pPhoneNumber;
                        String pEmail;
                        String newPublisher;
                        
                        stmt = conn.createStatement();
                        sql = "SELECT PublisherName FROM Publishers"; 
                        rs = stmt.executeQuery(sql);
                        
                        showAllPublisherNames(rs);
                        
                        // validate input to get an existing one

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter an existing Publisher name: ");
                            publisher = in.nextLine();
                            found = publisherFound(publisher, rs);
                        }
                        while(found == false);
                        
                        // validates user input so gets a publisher that doesn't exist so far
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter a new Publisher name: ");
                            newPublisher = in.nextLine();
                            found = publisherFound(newPublisher, rs);
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
                        
                        
                        // insert new user publisher into Publishers table
                        sql = "INSERT INTO Publishers (PublisherName, PublisherAddress, PublisherPhone, PublisherEmail) VALUES (?,?,?,?)";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.clearParameters();
                        pstmt.setString(1, newPublisher);
                        pstmt.setString(2, pAddress);
                        pstmt.setString(3, pPhoneNumber);
                        pstmt.setString(4, pEmail);
                        
                        numRows = pstmt.executeUpdate();
                        System.out.println("Number of Rows affected: " + numRows);
                        
                        System.out.println();
                        System.out.println("Books with old publisher name before adding new publishers");
                        rs = getBooksForSpecificPublisher(conn, publisher);
                        
                        showSelectedBookData(rs);
                        
                        System.out.println("Replacing books");
                        // replace books with old publisher name with the new publihser name that was just added
                        sql = "UPDATE Books SET PublisherName = ? "
                                + "WHERE PublisherName = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.clearParameters();
                        pstmt.setString(1, newPublisher);
                        pstmt.setString(2, publisher);
                        numRows = pstmt.executeUpdate();
                        System.out.println("Number of rows affected for replacing: " + numRows);
                        
                        System.out.println();
                        System.out.println("Books with new publisher name after replaced the old publisher name");
                        rs = getBooksForSpecificPublisher(conn, newPublisher);
                        
                        showSelectedBookData(rs);
                        
                        System.out.println();
                        stmt = conn.createStatement();
                        sql = "SELECT PublisherName FROM Books";
                        rs = stmt.executeQuery(sql);
                        
                        showAllPublisherNames(rs);
 
                        break;
                    case("9"):
                        
                        System.out.println();
                        showGroupNamesFromBooks(stmt, conn);
                        System.out.println();
                        
                        System.out.println();
                        // Show all available Publishers for user to choose
                        stmt = conn.createStatement();
                        sql = "SELECT Booktitle FROM Books";
                        rs = stmt.executeQuery(sql);
                        
                        showBookNames(rs);
                        System.out.println();
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName FROM WritingGroup";
 
                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Writing Group name: ");
                            groupName = in.nextLine();
                            System.out.println("GroupName: " + groupName);
                            found = writingGroupFound(groupName, rs);
                        }
                        while(found == false);
                        
                        stmt = conn.createStatement();
                        sql = "SELECT GroupName, BookTitle FROM Books";

                        do {
                            rs = stmt.executeQuery(sql);
                            System.out.println();
                            System.out.print("Please enter existing Book Title: ");
                            bookTitles = in.nextLine();
                            unique = existingBook(groupName, bookTitles, rs);
                        }
                        while(unique == true);
                        
                        // Book table before deleting row
                        System.out.println("Books table before deleting user selected book");
                        stmt = conn.createStatement();
                        rs = getAllBooksData(rs, stmt);
                        
                        sql = "DELETE FROM Books "
                                + "WHERE GroupName = ? "
                                + "AND BookTitle = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.clearParameters();
                        pstmt.setString(1, groupName);
                        pstmt.setString(2, bookTitles);
                        numRows = pstmt.executeUpdate();
                        System.out.println();
                        System.out.println("Number of rows deleted: " + numRows);
                        
                        System.out.println();
                        System.out.println("Books table after deleting user selected book");
                        stmt = conn.createStatement();
                        rs = getAllBooksData(rs, stmt);
                        
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
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
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
