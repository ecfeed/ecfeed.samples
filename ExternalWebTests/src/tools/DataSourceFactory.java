package tools;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;

public class DataSourceFactory {
	 
	public static DataSource getHSQLDataSource(){
		
		JDBCDataSource hsqlDS = null;

		hsqlDS = new JDBCDataSource();
		hsqlDS.setUrl("jdbc:hsqldb:hsql://localhost/broadleaf;ifexists=true");
		hsqlDS.setUser("sa");
		hsqlDS.setPassword("");

		return hsqlDS;
	}
	    
	        
}
