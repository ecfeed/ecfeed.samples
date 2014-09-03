package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tools.ConnectionInstance;
import tools.DataSourceFactory;
import tools.DriverFactory;
import tools.PageAddress;
import tools.Utils;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;
import com.testify.ecfeed.runner.annotations.expected;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestLogin {
	private WebDriver driver;
	private ConnectionInstance connection;
	private String baseUrl = PageAddress.Login;

  	/*
  	 * Tests for success.
  	 */
	@Test
	public void testLoginSuccess(String email, String password, String input_email, String input_password, @expected boolean valid_input) throws Exception {
		String escaped_email = Utils.escapeString(email);
		String escaped_password = Utils.escapeString(password);
		try {
			setUp();
			driver.get(baseUrl);

			/*
			 * We will combine ecFeed with in-test logic checks.
			 * It is sometimes needed when there are more than 1 levels of complexity.
			 * Here we have:
			 * - validation checks on input data (please note that email and password are already in base and must be valid)
			 * - logical check - when input data is valid AND is matching database data it results in success.
			 */
			
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10110;");
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					escaped_email +"','vname','vname','" + escaped_password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + escaped_email + "',NULL,NULL)");

			WebElement mail_field = driver.findElement(By.name("j_username"));
			Assert.assertTrue("field should be of email type!", mail_field.getAttribute("type").equalsIgnoreCase("email"));
			driver.findElement(By.name("j_username")).sendKeys(input_email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(input_password);
			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if (valid_input && (email.equals(input_email) && password.equals(input_password))) {
				Assert.assertTrue(isElementPresent(By.linkText("Logout")));
			} else if(valid_input){
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));	
			} else{
				Assert.assertTrue("Login failed", isElementPresent(By.linkText("Login")));
			}
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}

	/*
	 * Test for email field warnings.

	 */
	@Test
	public void testLoginEmail(String email, String input_email, boolean expected_result) throws Exception {
		String escaped_email = Utils.escapeString(email);
		String password = "mypassword";
		try {
			setUp();
			driver.get(baseUrl);

			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10110;");
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					escaped_email +"','vname','vname','" + password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + escaped_email + "',NULL,NULL)");

			WebElement mail_field = driver.findElement(By.name("j_username"));
			Assert.assertTrue("field should be of email type!", mail_field.getAttribute("type").equalsIgnoreCase("email"));
			driver.findElement(By.name("j_username")).sendKeys(input_email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(password);
			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if (expected_result && email.equals(input_email)) {
				Assert.assertTrue( isElementPresent(By.linkText("Logout")));
			} else if (expected_result){
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			} else {
				Assert.assertTrue("Login failed", isElementPresent(By.linkText("Login")));
			}
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}
	
	/*
	 * Test for password field warnings.
	 * Lack of compare constraint (arg1 = / != arg2) makes it tempting to use mixed solution, as in LoginSuccess test.
	 * In this one we will use just EcFeed though.
	 */
	@Test
	public void testLoginPassword(String password, String input_password, boolean expected_result) throws Exception {
		String escaped_password = Utils.escapeString(password);
		String email = "email@mail.com";
		try {
			setUp();
			driver.get(baseUrl);

			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10110;");
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					email +"','vname','vname','" + escaped_password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + email + "',NULL,NULL)");

			WebElement mail_field = driver.findElement(By.name("j_username"));
			Assert.assertTrue("field should be of email type!", mail_field.getAttribute("type").equalsIgnoreCase("email"));
			driver.findElement(By.name("j_username")).sendKeys(email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(input_password);
			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if (expected_result) {
				Assert.assertTrue(isElementPresent(By.linkText("Logout")));
			} else {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			}
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}
  
	private void setUp(){
		try {
			connection = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
		} catch(Exception e) {
			e.printStackTrace();
			throw new Error("Failed to initialize database connection");
		}

		try {
			driver = DriverFactory.getDriver();
		} catch(Exception e) {
			throw new Error("Failed to initialize Selenium driver");
		}
			
	}

	private void tearDown() throws Exception {
		if (driver != null) {
			driver.quit();
		}
		if (connection != null) {
			connection.close();
		}
	}
	
	private void cleanUpAfterTest(String email){
		try{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + Utils.escapeString(email) + "';");
		} catch(Exception e){
			throw new Error("Database connection failed");
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
}
