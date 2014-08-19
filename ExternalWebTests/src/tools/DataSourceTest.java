package tools;

import java.sql.SQLException;

public class DataSourceTest{

	public static void main(String[] args){
		testPrivileges();
		System.out.println("\n**********\n");
		testDataSource();
	}

	private static void testPrivileges(){
		ConnectionInstance con = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());

		System.out.println("Testing DBA privileges...");
		// #################################
		con.tryQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE;");
		con.tryQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE;");
		System.out.println("DBA privileges granted.");
		// ##################################
		con.close();
	}

	private static void testDataSource(){

		ConnectionInstance con = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());

		try{
			con.tryQuery("SELECT * FROM PUBLIC.blc_customer;");
			while(con.result.next()){
				System.out.println("Customer ID=" + con.result.getInt("CUSTOMER_ID") + ", " + con.result.getString("FIRST_NAME"));
			}
			// #################################
			con.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE FIRST_NAME='vname';");

			System.out.println("After deletion:");

			con.tryQuery("SELECT * FROM PUBLIC.blc_customer;");
			while(con.result.next()){
				System.out.println("Customer ID=" + con.result.getInt("CUSTOMER_ID") + ", " + con.result.getString("FIRST_NAME"));
			}
			System.out.println("\nDone.");
			// ##################################

		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			if(con != null){
				con.close();
			}
		}
	}

}
