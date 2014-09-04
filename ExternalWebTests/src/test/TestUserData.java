package test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;
import tools.DriverFactory;
import tools.PageAddress;
import tools.Utils;

public class TestUserData{
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
	
	protected void login(String email, String password){
		driver.get(PageAddress.Login);
		driver.findElement(By.name("j_username")).sendKeys(email);
		driver.findElement(By.name("j_password")).clear();
		driver.findElement(By.name("j_password")).sendKeys(password);
		driver.findElement(By.xpath("//input[@value='Login']")).click();
	}

	protected void tearDown() throws Exception{
		if(driver != null){
			driver.quit();
		}
		if(connection != null){
			connection.close();
		}
	}

	protected void cleanUpAfterTest(String email){
		try{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + Utils.escapeString(email) + "';");
		} catch(Exception e){
			throw new Error("Database connection failed");
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
