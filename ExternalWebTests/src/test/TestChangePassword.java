package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import tools.ConnectionInstance;
import tools.DataSourceFactory;
import tools.DriverFactory;
import tools.PageAddress;
import tools.Utils;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestChangePassword {
	private WebDriver driver;
	private ConnectionInstance connection;
	private String baseUrl = PageAddress.Login;
	
	@Test
	public void testChangePasswordSuccess(String password, String currentpsswd, String newpassword, String confnewpsswd) throws Exception{
		String email = "standard.email@address.com";
		String first_name = "Firstname";

		try{
			setUp();
			tryLoginAndChangePassword(email, first_name, password, currentpsswd, newpassword, confnewpsswd);
			driver.findElement(By.linkText("Logout")).click();
			Assert.assertTrue("Not logged out!", isElementPresent(By.linkText("Login")));

			driver.get(baseUrl);
			driver.findElement(By.name("j_username")).clear();
			driver.findElement(By.name("j_username")).sendKeys(email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(newpassword);
			driver.findElement(By.xpath("//input[@value='Login']")).click();

			if(((password.equals(currentpsswd) && newpassword.equals(confnewpsswd)) && !newpassword.equals(currentpsswd))){
				Assert.assertFalse("Unexpected error!", isElementPresent(By.className("error")));
				Assert.assertTrue("Not logged on!", isElementPresent(By.linkText(first_name)));
			} else{
				Assert.assertFalse("Shouldn't be logged on!", isElementPresent(By.linkText(first_name)));
			}
		} finally{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
			tearDown();
		}

	}
	
	@Test
	public void testChangePasswordWarnings(String password, String currentpsswd, String newpassword,
			String confnewpsswd) throws Exception {
		String email = "standard.email@address.com";
		String first_name = "Firstname";
		
		try {
			setUp();			
			tryLoginAndChangePassword(email, first_name, password, currentpsswd, newpassword, confnewpsswd);
			
			if(currentpsswd.equals("")){
				Assert.assertTrue("Expected EnterCurrentPassword here!", isElementPresent(By.xpath("//*[contains(.,'"+ErrorMessage.EnterCurrentPassword+"')]")));
			}
			else if(newpassword.equals("")){
				Assert.assertTrue("Expected EnterNewPassword here!", isElementPresent(By.xpath("//*[contains(.,'"+ErrorMessage.EnterNewPassword+"')]")));
			}
			else if(confnewpsswd.equals("")){
				Assert.assertTrue("Expected EnterNewPassword here!", isElementPresent(By.xpath("//*[contains(.,'"+ErrorMessage.ConfirmNewPassword+"')]")));	
			}
			else if(!(currentpsswd.equals(password) && newpassword.equals(currentpsswd))){
				Assert.assertTrue("Expected error here!", isElementPresent(By.className("error")));
				Assert.assertTrue("Expected InvalidCurrentPassword here!", isElementPresent(By.xpath("//*[contains(.,'"+ErrorMessage.InvalidCurrentPassword+"')]")));
			}
			if(((password.equals(currentpsswd) && newpassword.equals(confnewpsswd)) && !newpassword.equals(currentpsswd))){
				Assert.assertFalse("Unexpected error!", isElementPresent(By.className("error")));
			} else{
				Assert.assertTrue("Expected error here!", isElementPresent(By.className("error")));
			}						
		} finally {
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
			tearDown();
		}
	
	}
	
	private void tryLoginAndChangePassword(String email, String first_name, String password, String currentpsswd, String newpassword,
			String confnewpsswd){
		connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
		connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10112;");
		connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10112,10112,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
				email +"','"+Utils.escapeString(first_name)+"','vname','" + Utils.escapeString(password) +"{10112}',FALSE,NULL,TRUE,TRUE,NULL,'" + email + "',NULL,NULL)");
		
		driver.get(baseUrl);

		driver.findElement(By.name("j_username")).sendKeys(email);
		driver.findElement(By.name("j_password")).clear();
		driver.findElement(By.name("j_password")).sendKeys(password);
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		
		Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(first_name)));
		
		driver.findElement(By.linkText(first_name)).click();
		driver.findElement(By.linkText("Change Password")).click();
		driver.findElement(By.id("currentPassword")).clear();
		driver.findElement(By.id("currentPassword")).sendKeys(currentpsswd);
		driver.findElement(By.id("newPassword")).clear();
		driver.findElement(By.id("newPassword")).sendKeys(newpassword);
		driver.findElement(By.id("newPasswordConfirm")).clear();
		driver.findElement(By.id("newPasswordConfirm")).sendKeys(confnewpsswd);
		driver.findElement(By.cssSelector("input.medium.red")).click();
	}
	
	/*
	 * This test requires valid password. It tries to register instead of injecting data explicitly into db.
	 */
//	@Test
//	public void testRegisterAndChangePassword(String password, String currentpsswd, String newpassword,
//			String confnewpsswd, boolean expected_result) throws Exception {
//		
//		String email = "standard.email@address.com";
//		String first_name = "Firstname";
//		
//		try {
//			setUp();
//			driver.get(baseUrl);
//
//			driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
//			driver.findElement(By.id("customer.emailAddress")).clear();
//			driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
//			driver.findElement(By.id("customer.firstName")).clear();
//			driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
//			driver.findElement(By.id("customer.lastName")).clear();
//			driver.findElement(By.id("customer.lastName")).sendKeys("Surname");
//			driver.findElement(By.id("password")).clear();
//			driver.findElement(By.id("password")).sendKeys(password);
//			driver.findElement(By.id("passwordConfirm")).clear();
//			driver.findElement(By.id("passwordConfirm")).sendKeys(password);
//			driver.findElement(By.xpath("//input[@value='Register']")).click();
//			
//			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(first_name)));
//			driver.findElement(By.linkText(first_name)).click();
//			driver.findElement(By.linkText("Change Password")).click();
//			driver.findElement(By.id("currentPassword")).clear();
//			driver.findElement(By.id("currentPassword")).sendKeys(password);
//			driver.findElement(By.id("newPassword")).clear();
//			driver.findElement(By.id("newPassword")).sendKeys(newpassword);
//			driver.findElement(By.id("newPasswordConfirm")).clear();
//			driver.findElement(By.id("newPasswordConfirm")).sendKeys(confnewpsswd);
//			driver.findElement(By.cssSelector("input.medium.red")).click();
//			driver.findElement(By.cssSelector("a > span")).click();
//			
//			Assert.assertTrue("Not logged out!", isElementPresent(By.linkText("Login")));
//
//			driver.findElement(By.cssSelector("span")).click();
//			driver.findElement(By.name("j_username")).clear();
//			driver.findElement(By.name("j_username")).sendKeys(email);
//			driver.findElement(By.name("j_password")).clear();
//			driver.findElement(By.name("j_password")).sendKeys(newpassword);
//			driver.findElement(By.xpath("//input[@value='Login']")).click();
//			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(first_name)));
//		} finally {
//			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + Utils.escapeString(email) + "';");
//			tearDown();
//		}
//	}

	private void setUp() throws Exception {
		try {
			connection = new ConnectionInstance(DataSourceFactory.getHSQLDataSource());
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer");
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

	private void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		if (connection != null) {
			connection.close();
		}
	}

	private boolean isElementPresent(By by) {
		try{
			driver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}
}
