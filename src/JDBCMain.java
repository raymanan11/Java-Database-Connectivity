

import java.sql.*;
import java.util.Scanner;

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
        System.out.println("2.) List Data for Specified Group");
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
        char continues;
        int selection;
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
        Connection conn = null; //initialize the connection
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
//            stmt = conn.createStatement();
//            String sql;
//            sql = "SELECT au_id, au_fname, au_lname, phone FROM Authors";
//            ResultSet rs = stmt.executeQuery(sql);
            
            continues = 'a';
            
            while (continues != 'q') {
                showMenu();
                
                System.out.print("Choose your number selection: ");
                selection = in.nextInt();
                
                switch(selection) {
                    case(1):
                        System.out.println("Creating statement...");
                        stmt = conn.createStatement();
                        String sql;
                        sql = "SELECT * FROM WritingGroup";
                        rs = stmt.executeQuery(sql);
                        
                        while (rs.next()) {
                            //Retrieve by column name
                            String groupName = rs.getString("GroupName");
                            String headWriter = rs.getString("HeadWriter");
                            String yearFormed = rs.getString("YearFormed");
                            String subject = rs.getString("Subject");

                            //Display values
                            System.out.print("Group name: " + groupName + " ");
                            System.out.print("Head Writer: " + headWriter + " ");
                            System.out.print("Year Formed: " + yearFormed + " ");
                            System.out.print("Subject: " + subject);
                            System.out.println();
                        }  
            //STEP 6: Clean-up environment
                        break;
                    case(2):
                        break;
                    case(3):
                        break;
                    case(4):
                        break;    
                    case(5):
                        break;
                    case(6):
                        break;  
                    case(7):
                        break;
                    case(8):
                        break;
                    case(9):
                        break;
                    default:
                        System.out.println("Invalid selection");
                            
            }
                
                System.out.println("Press any key to continue or q to quit");
                continues = in.next().charAt(0);
                // ask for continues input again at very end of loop
            }

            //STEP 5: Extract data from result set
//            System.out.printf(displayFormat, "ID", "First Name", "Last Name", "Phone #");
//            while (rs.next()) {
//                //Retrieve by column name
//                String id = rs.getString("au_id");
//                String phone = rs.getString("phone");
//                String first = rs.getString("au_fname");
//                String last = rs.getString("au_lname");
//
//                //Display values
//                System.out.printf(displayFormat,
//                        dispNull(id), dispNull(first), dispNull(last), dispNull(phone));
//            }
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
