package tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import javax.sql.DataSource;
 
public class DataSourceTest {
 
    public static void main(String[] args) {
         
        testDataSource();
        System.out.println("**********");
 
    }
 
    private static void testDataSource() {
        DataSource ds = DataSourceFactory.getHSQLDataSource();
         
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM broadleaf.blc_customer;");
            while(rs.next()){
                System.out.println("Customer ID="+rs.getInt("CUSTOMER_ID"));
            }
            //#################################
            stmt.executeQuery("SET FOREIGN_KEY_CHECKS=0;");
            stmt.executeUpdate("DELETE FROM broadleaf.blc_customer WHERE FIRST_NAME=\"vnametwo\";");
            stmt.executeQuery("SET FOREIGN_KEY_CHECKS=1;");
            rs.close();
            
            System.out.println("After deletion:");
            
            rs = stmt.executeQuery("SELECT * FROM broadleaf.blc_customer;");
            while(rs.next()){
                System.out.println("Customer ID="+rs.getInt("CUSTOMER_ID"));
            }
            //##################################
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                try {
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                    if(con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
 
}
