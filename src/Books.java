
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author raymanan11
 */
public class Books {
    
    PreparedStatement pstmt = null;
    WritingGroup wg = new WritingGroup();
    Publishers publishers = new Publishers();
    
    public boolean existingBook(String groupName, String bookTitle, ResultSet rs) throws SQLException {
        boolean uniqueBook = true;
        String gName;
        String bTitle;
        String pName;
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

    public boolean matchingTitleAndPublisherName(String bookTitle, String publisherName, ResultSet rs, Connection conn) throws SQLException {
        boolean uniqueCombo = true;
        boolean foundPName = false;
        String bTitle;
        String pName;

        Statement stmt = conn.createStatement();
        String sql = "SELECT PublisherName FROM Publishers";
        ResultSet resultSet = stmt.executeQuery(sql);
        foundPName = publishers.publisherFound(publisherName, resultSet);
        
        if (!foundPName) {
            System.out.println("Entered publisher name doesn't exist in database!");
            return false;
        }
        
        while (rs.next()) {
            bTitle = rs.getString("BookTitle");
            pName = rs.getString("PublisherName");
            if (bTitle.equals(bookTitle) && pName.equals(publisherName)) {
                System.out.println("Book Title and Publisher name already exists, please enter a diffeent Publisher name");
                uniqueCombo = false;
                break;
            }
        }
        return uniqueCombo;
    }
    
    public void showBookNames(ResultSet rs) throws SQLException {
        System.out.println("Available Books: ");
        System.out.println();
        while (rs.next()) {
            String bookTitles = rs.getString("BookTitle");
            System.out.println(bookTitles);
        }
    }
    
    public ResultSet getAndShowAllBooksData(ResultSet rs, Statement stmt) throws SQLException {
        String sql;
        sql = "SELECT * FROM Books";
        rs = stmt.executeQuery(sql);
        showAllBooksData(rs);
        return rs;
    }
    
        public Statement showGroupNamesFromBooks(Statement stmt, Connection conn) throws SQLException {
        String sql;
        ResultSet rs;
        // Show all available Writing Groups for user to choose
        stmt = conn.createStatement();
        sql = "SELECT GroupName FROM Books";
        rs = stmt.executeQuery(sql);
        wg.showWritingGroupNames(rs);
        return stmt;
    }
    
    public void showAllBooksData(ResultSet rs) throws SQLException {
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
    
    public void showSelectedBookData(ResultSet rs) throws SQLException {
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
    
    public void getAndShowBookTitles(ResultSet rs, Statement stmt) throws SQLException {
        String sql;
        sql = "SELECT Booktitle FROM Books";
        rs = stmt.executeQuery(sql);
        showBookNames(rs);
    }
    
    public void insertIntoBooksTable(Connection conn, String groupName, String bookTitles, String publisherName, int yearPublished, int numberPages) throws SQLException {
        String sql;
        int numRows;
        sql = "INSERT INTO Books (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) VALUES (?,?,?,?,?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, groupName);
        pstmt.setString(2, bookTitles);
        pstmt.setString(3, publisherName);
        pstmt.setInt(4, yearPublished);
        pstmt.setInt(5, numberPages);
        numRows = pstmt.executeUpdate();
        System.out.println("Number of Rows added in Book table: "  + numRows);
    }
    
    public void removeFromBooks(Connection conn, String groupName, String bookTitles) throws SQLException {
        String sql;
        int numRows;
        
        sql = "DELETE FROM Books "
                + "WHERE GroupName = ? "
                + "AND BookTitle = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, groupName);
        pstmt.setString(2, bookTitles);
        
        numRows = pstmt.executeUpdate();
        System.out.println();
        System.out.println("Number of rows deleted from Books: " + numRows);
    }
    
    public void closePreparedStatement() throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
