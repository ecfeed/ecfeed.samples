package test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;
import tools.DriverFactory;

public class ParentTest{
	protected WebDriver driver;
	protected ConnectionInstance connectionInstance;
	protected String baseUrl;

	protected void setUp(){
		try{
			connectionInstance = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Failed to initialize database connection.");
		}
		
		if (connectionInstance.connection == null) {
			throw new Error("No database connection.");
		}

		try{
			driver = DriverFactory.getDriver();
			driver.manage().window().maximize();
		} catch(Exception e){
			throw new Error("Failed to initialize Selenium driver.");
		}

	}
	
	protected void tearDown() throws Exception{
		if(driver != null){
			driver.quit();
		}
		if(connectionInstance != null){
			connectionInstance.close();
		}
	}
	
	protected boolean isElementPresent(By by){
		try{
			driver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}
}
