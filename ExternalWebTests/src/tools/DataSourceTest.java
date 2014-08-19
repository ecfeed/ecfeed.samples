package tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import javax.sql.DataSource;
 
public class DataSourceTest {
 
    public static void main(String[] args) {
        //testPrivileges(); 
    	//testTablePresence();
        testDataSource();
        System.out.println("**********");
 
    }
    
    private static void testPrivileges() {
		DataSource ds = DataSourceFactory.getHSQLDataSource();

		Connection con = null;
		Statement stmt = null;
		try{
			con = ds.getConnection();
			stmt = con.createStatement();
			// #################################
			stmt.executeQuery("SET FOREIGN_KEY_CHECKS=0;");
			stmt.executeQuery("SET FOREIGN_KEY_CHECKS=1;");
			// ##################################
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                try {
                    if(stmt != null) stmt.close();
                    if(con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
 
    private static void testTablePresence() {
		DataSource ds = DataSourceFactory.getHSQLDataSource();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES;");
			while(rs.next()){
				System.out.println(rs.getString(2));
			}
			// #################################
			stmt.executeQuery("SET FOREIGN_KEY_CHECKS=0;");
			stmt.executeUpdate("DELETE FROM broadleaf.blc_customer WHERE FIRST_NAME=\"vnametwo\";");
			stmt.executeQuery("SET FOREIGN_KEY_CHECKS=1;");
			rs.close();

			System.out.println("After deletion:");

			rs = stmt.executeQuery("SELECT * FROM broadleaf.blc_customer;");
			while(rs.next()){
				System.out.println("Customer ID=" + rs.getInt("CUSTOMER_ID"));
			}
			// ##################################
            
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
    
    private static void testDataSource() {
        DataSource ds = DataSourceFactory.getHSQLDataSource();
         
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PUBLIC.blc_customer;");
            while(rs.next()){
                System.out.println("Customer ID="+rs.getInt("CUSTOMER_ID"));
            }
            //#################################
            stmt.executeQuery("SET FOREIGN_KEY_CHECKS=0;");
            stmt.executeUpdate("DELETE FROM PUBLIC.blc_customer WHERE FIRST_NAME=\"vnametwo\";");
            stmt.executeQuery("SET FOREIGN_KEY_CHECKS=1;");
            rs.close();
            
            System.out.println("After deletion:");
            
            rs = stmt.executeQuery("SELECT * FROM PUBLIC.blc_customer;");
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
