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

	/*
	 * Testing statements here.
	 */
	private static void testDataSource(){

		ConnectionInstance con = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		
		String email = "'que@que.que'";

		con.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,"+
				email +",'vname','vname'," + "'mypass{10110}',FALSE,NULL,TRUE,TRUE,NULL," + email + ",NULL,NULL)");
		
		try{
			con.tryQuery("SELECT * FROM PUBLIC.blc_customer;");
			while(con.fResult.next()){
				System.out.println("Customer ID=" + con.fResult.getInt("CUSTOMER_ID") + ", " + con.fResult.getString("FIRST_NAME") + ", " +
						con.fResult.getString("EMAIL_ADDRESS") + ", " + con.fResult.getString("PASSWORD"));
			}
			// #################################
			con.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE FIRST_NAME='vname';");

			System.out.println("After deletion:");

			con.tryQuery("SELECT * FROM PUBLIC.blc_customer;");
			while(con.fResult.next()){
				System.out.println("Customer ID=" + con.fResult.getInt("CUSTOMER_ID") + ", " + con.fResult.getString("FIRST_NAME") + ", " +
						con.fResult.getString("EMAIL_ADDRESS") + ", " + con.fResult.getString("PASSWORD"));
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
