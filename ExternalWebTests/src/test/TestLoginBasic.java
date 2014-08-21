package test;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestLoginBasic {
  private WebDriver driver;
  private String baseUrl = "http://localhost:8080/login";
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  private ConnectionInstance connection;
  
  	@Test
  	public void testBrowserLoginCheck(String email, boolean expected_result) throws Exception{
		try{
			setUp();

			driver.get(baseUrl);

			WebElement mail_field = driver.findElement(By.name("j_username"));
			Assert.assertTrue("field should be of email type!", mail_field.getAttribute("type").equalsIgnoreCase("email"));
			
			driver.findElement(By.name("j_username")).clear();
			driver.findElement(By.name("j_username")).sendKeys(email);

			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if(expected_result){
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.loginNotFound + "')]")));
			} else{
				Assert.assertFalse(isElementPresent(By.className("error")));//"No error message present", driver.findElement(By.linkText("Logout")) == null);
			}
		} finally{
			tearDown();
		}
  	}

  
  	//password is stored without hashing, in format:	password{USER_ID}
	@Test
	public void testLoginEmailData(String email, String password, boolean expected_result) throws Exception{
		try{
			setUp();
			
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					email +"','vname','vname','" + password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + email + "',NULL,NULL)");
			
			driver.get(baseUrl);

			WebElement mail_field = driver.findElement(By.name("j_username"));
			Assert.assertTrue("field should be of email type!", mail_field.getAttribute("type").equalsIgnoreCase("email"));

			driver.findElement(By.name("j_username")).sendKeys(email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(password);
			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if(expected_result){
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessages.loginNotFound + "')]"))
						|| driver.findElement(By.linkText("Logout")) != null);
			} else{
				Assert.assertTrue("Login failed", driver.findElement(By.linkText("Login")) != null);
			}
			
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
			
		} finally{
			tearDown();
		}

	}
  
	private void setUp(){
		try{
			connection = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Failed to initialize database connection");
		}

		try{
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		} catch(Exception e){
			throw new Error("Failed to initialize Selenium driver");
		}
			
	}

	private void tearDown() throws Exception{
		if(driver != null){
			driver.quit();
		}
		if(connection != null){
			connection.close();
		}

		String verificationErrorString = verificationErrors.toString();
		if(!"".equals(verificationErrorString)){
			fail(verificationErrorString);
		}
	}

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
