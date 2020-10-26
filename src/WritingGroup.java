
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author raymanan11
 */
public class WritingGroup {
    
    public boolean writingGroupFound(String writingGroup, ResultSet rs) throws SQLException {
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
    
    public void showWritingGroupNames(ResultSet rs) throws SQLException {
        System.out.println("Available Writing Groups: ");
        System.out.println();
        while (rs.next()) {
            String groupName = rs.getString("GroupName");
            System.out.println(groupName);
        }
    }
    
    public void getAndShowGroupNames(Statement stmt, Connection conn) throws SQLException {
        String sql;
        ResultSet rs;
        // Show all available Writing Groups names for user to choose
        stmt = conn.createStatement();
        sql = "SELECT GroupName FROM WritingGroup";
        rs = stmt.executeQuery(sql);
        showWritingGroupNames(rs);
    }
    
    public void showAllWritingGroupData(ResultSet rs) throws SQLException {
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
}
