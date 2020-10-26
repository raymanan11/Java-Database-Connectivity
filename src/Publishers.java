
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
public class Publishers {
    
    PreparedStatement pstmt;
    Data data = new Data();
    
    public boolean publisherFound(String publisherz, ResultSet rs) throws SQLException {
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
    
    public ResultSet getAndShowPublisherNames(Statement stmt, Connection conn) throws SQLException {
        String sql;
        ResultSet rs;
        // Show all available Publisher names for user to choose
        stmt = conn.createStatement();
        sql = "SELECT PublisherName FROM Publishers";
        rs = stmt.executeQuery(sql);         
        showPublisherNames(rs);
        return rs;
    }
    
    public void showAllPublishersData(ResultSet rs) throws SQLException {
        
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
    
    public void showPublisherNames(ResultSet rs) throws SQLException {
        System.out.println("Available Publishers: ");
        System.out.println();
        while (rs.next()) {
            String publisherName = rs.getString("PublisherName");
            System.out.println(publisherName);
        }
    }
    
    public void getSpecificPublisherData(String currentVariable, Connection conn, String publisherName, ResultSet rs) throws SQLException {
        String sql;
        sql = "SELECT * FROM Publishers "
                + "LEFT OUTER JOIN Books using (PublisherName) "
                + "LEFT OUTER JOIN WritingGroup using (GroupName) "
                + "WHERE " + currentVariable + " = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, publisherName);
        rs = pstmt.executeQuery();
        data.showSelectedData(rs);
    }
    
    
    public ResultSet getAndShowAllPublisherData(ResultSet rs, Statement stmt) throws SQLException {
        String sql;
        sql = "SELECT * FROM Publishers";
        rs = stmt.executeQuery(sql);
        showAllPublishersData(rs);
        return rs;
    }
    
    public ResultSet getBooksForSpecificPublisher(Connection conn, String publisher) throws SQLException {
        String sql;
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
        
    public void insertIntoPublishers(Connection conn, String newPublisher, String pAddress, String pPhoneNumber, String pEmail) throws SQLException {
        String sql;
        int numRows;
        // insert new user publisher into Publishers table
        sql = "INSERT INTO Publishers (PublisherName, PublisherAddress, PublisherPhone, PublisherEmail) VALUES (?,?,?,?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, newPublisher);
        pstmt.setString(2, pAddress);
        pstmt.setString(3, pPhoneNumber);
        pstmt.setString(4, pEmail);
        numRows = pstmt.executeUpdate();
        System.out.println("Number of Rows inserted into Publishers: " + numRows);
    }
    
    public void updatePublisherName(Connection conn, String newPublisher, String publisher) throws SQLException {
        String sql;
        int numRows;
        // replace books with old publisher name with the new publihser name that was just added
        sql = "UPDATE Books SET PublisherName = ? "
                + "WHERE PublisherName = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, newPublisher);
        pstmt.setString(2, publisher);
        numRows = pstmt.executeUpdate();
        System.out.println("Number of rows affected after replacing old publisher with new publisher: " + numRows);
    }
    
    public void removePublisher(Connection conn, String oldPublisher) throws SQLException {
        String sql;
        int numRows;
        
        sql = "DELETE FROM Publishers "
                + "WHERE PublisherName = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, oldPublisher);
        
        numRows = pstmt.executeUpdate();
        System.out.println();
        System.out.println("Deleting " + oldPublisher + " from Publisher Table in database!");
        System.out.println("Number of rows deleted from Publishers: " + numRows);
    }
    
    public void closePreparedStatement() throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
