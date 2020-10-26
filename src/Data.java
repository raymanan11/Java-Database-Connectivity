
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author raymanan11
 */
public class Data {
    
    PreparedStatement pstmt = null;
    
    public ResultSet getSpecificData(String currentVariable, Connection conn, String name, ResultSet rs) throws SQLException {
        String sql;
        sql = "SELECT * FROM WritingGroup "
                + "LEFT OUTER JOIN Books using (GroupName) "
                + "LEFT OUTER JOIN Publishers using (PublisherName) "
                + "WHERE " + currentVariable + " = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, name);
        rs = pstmt.executeQuery();
        showSelectedData(rs);
        return rs;
    }

    public void showSelectedData(ResultSet rs) throws SQLException {
        boolean correctInput = false;
        while (rs.next()) {
            correctInput = true;
            String groupName = rs.getString("GroupName");
            String headWriter = rs.getString("HeadWriter");
            int yearFormed = rs.getInt("YearFormed");
            String subject = rs.getString("Subject");

            //Display values
            System.out.println("WRITING GROUP:");
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Head Writer: " + headWriter + "   ");
            System.out.print("Year Formed: " + yearFormed + "   ");
            System.out.print("Subject: " + subject);
            System.out.println();
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
            System.out.println();
            
            String bookTitle = rs.getString("BookTitle");
            int yearPublished = rs.getInt("YearPublished");
            int numberPages = rs.getInt("NumberPages");

            //Display values
            System.out.println("BOOKS:");
            System.out.print("Group name: " + groupName + "   ");
            System.out.print("Publisher Name: " + publisherName + "   ");
            System.out.print("Book Title: " + bookTitle + "   ");
            System.out.print("Year Published: " + yearPublished + "   ");
            System.out.print("Number of Pages: " + numberPages + "   ");
            System.out.println();
            System.out.println();
        }
        
        if (!correctInput) {
            System.out.println("Wrong name given!");
        }
    }
    
    public void closePreparedStatement() throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
