package tools;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DBUtils{

	public static String escapeString(String input){
		return input.replace("'", "''");
	}

	public static void printTableContent(String tableName){
		ConnectionInstance connection;
		System.out.println("\nContents of table " + tableName + ":");
		try{
			connection = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());

			ResultSet rs = connection.tryQuery("SELECT * FROM " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();

			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				if(i > 1)
					System.out.print("\t");
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName);
			}

			while(rs.next()){
				System.out.println("");
				for(int i = 1; i <= rsmd.getColumnCount(); i++){
					if(i > 1)
						System.out.print("\t");
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Failed to initialize database connection");
		}
	}
}
