package tools;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;

public class DataSourceFactory {
	 
	public static DataSource getHSQLDataSource(){
		JDBCDataSource hsqlDS = null;

		hsqlDS = new JDBCDataSource();
		hsqlDS.setUrl("jdbc:hsqldb:file:test;shutdown=true");
		hsqlDS.setUser("sa");
		hsqlDS.setPassword("null");

		return hsqlDS;
	}
	    
	        
}
