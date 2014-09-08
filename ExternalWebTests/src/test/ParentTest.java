package test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;
import tools.DriverFactory;

public class ParentTest{
	protected WebDriver driver;
	protected ConnectionInstance connection;
	protected String baseUrl;

	protected void setUp(){
		try{
			connection = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Failed to initialize database connection");
		}

		try{
			driver = DriverFactory.getDriver();
		} catch(Exception e){
			throw new Error("Failed to initialize Selenium driver");
		}

	}
	
	protected void tearDown() throws Exception{
		if(driver != null){
			driver.quit();
		}
		if(connection != null){
			connection.close();
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
