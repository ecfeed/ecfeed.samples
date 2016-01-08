package test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;
import tools.DriverFactory;

public class ParentTest{
	protected WebDriver fDriver;
	protected ConnectionInstance fConnectionInstance;
	protected String fBaseUrl;

	protected void setUp(){
		try{
			fConnectionInstance = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Failed to initialize database connection.");
		}
		
		if (fConnectionInstance.fConnection == null) {
			throw new Error("No database connection.");
		}

		try{
			fDriver = DriverFactory.getDriver();
			fDriver.manage().window().maximize();
		} catch(Exception e){
			throw new Error("Failed to initialize Selenium driver.");
		}

	}
	
	protected void tearDown() throws Exception{
		if(fDriver != null){
			fDriver.quit();
		}
		if(fConnectionInstance != null){
			fConnectionInstance.close();
		}
	}
	
	protected boolean isElementPresent(By by){
		try{
			fDriver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}
}
