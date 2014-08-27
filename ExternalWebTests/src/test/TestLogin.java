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
  	 * This one is for pairwise.
  	 * Of course all tests could be covered with just this method, but it will require some clever constraints.
  	 */
	@Test
	public void testLoginData(String email, String password, String input_email, String input_password, @expected boolean expected_result) throws Exception {
		String escaped_email = Utils.escapeString(email);
		String escaped_password = Utils.escapeString(password);
		try {
			setUp();
			driver.get(baseUrl);

			/* just wondering:
			 * it should be easy to just dump expected_result and put an IF(email == input_email && password == input_password)
			 * then condition assert based on result (assert login succeed if they match, assert false if not)
			 * but then it would take away all the control user has via ecFeed so it would be just used
			 * as data generating tool. So I will rather leave expected result and juggle around with constraints,
			 * we are after presenting features, not simplicity, after all.
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

			if (expected_result) {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]"))
						|| driver.findElement(By.linkText("Logout")) != null);
			} else{
				Assert.assertTrue("Login failed", driver.findElement(By.linkText("Login")) != null);
			}
		} finally {
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + escaped_email + "';");
			tearDown();
		}
	}

	@Test
	public void testLoginEmailData(String email, boolean expected_result) throws Exception {
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
			driver.findElement(By.name("j_username")).sendKeys(email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(password);
			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if (expected_result) {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]"))
						|| driver.findElement(By.linkText("Logout")) != null);
			} else {
				Assert.assertTrue("Login failed", driver.findElement(By.linkText("Login")) != null);
			}
		} finally {
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + escaped_email + "';");
			tearDown();
		}
	}
	
	@Test
	public void testLoginPasswordData(String password, String input_password, boolean expected_result) throws Exception {
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
				Assert.assertTrue(driver.findElement(By.linkText("Logout")) != null);
			} else {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			}
		} finally {
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
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

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
