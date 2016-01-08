package tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;



public class ConnectionInstance{
	public Connection fConnection = null;
	public Statement fStatement = null;
	public ResultSet fResult = null;
	
	public ConnectionInstance(DataSource dataSource){
		try{
			fConnection = dataSource.getConnection();
			fStatement = fConnection.createStatement();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ResultSet tryQuery(String query){
		try{
			fResult = fStatement.executeQuery(query);
			return fResult;
		} catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("While executing query: ");
			System.out.println(query);
			fResult = null;
			return null;
		}		
	}
	
	public int tryUpdate(String query){
		int outcome = -1;
		try{
			fStatement.executeQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE;");
			outcome = fStatement.executeUpdate(query);
			fStatement.executeQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE;");
			return outcome;
		} catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("While executing query: ");
			System.out.println(query);
			return outcome;
		}		
	}
	
	public void close(){
		if (fConnection == null) {
			return;
		}
		try{
			fConnection.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
