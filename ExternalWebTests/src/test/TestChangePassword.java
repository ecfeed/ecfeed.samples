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
import com.testify.ecfeed.runner.annotations.*;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestComplexLoginAndChangePassword {
	private WebDriver driver;
	private ConnectionInstance connection;
	private String baseUrl = PageAddress.Base;

	@Test
	public void testRegisterBasic(String email, String first_name, String last_name, String password, String confpsswd, String newpassword,
			String confnewpsswd, boolean valid_data) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);

			driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
			driver.findElement(By.id("customer.emailAddress")).clear();
			driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
			driver.findElement(By.id("customer.firstName")).clear();
			driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
			driver.findElement(By.id("customer.lastName")).clear();
			driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(password);
			driver.findElement(By.id("passwordConfirm")).clear();
			driver.findElement(By.id("passwordConfirm")).sendKeys(confpsswd);
			driver.findElement(By.xpath("//input[@value='Register']")).click();

			boolean email_taken = isElementPresent(By.xpath("//[contains(.,'" + ErrorMessage.AddressInUse + "')]"));
			if (email_taken) {
				driver.findElement(By.cssSelector("span")).click();
				driver.findElement(By.name("j_username")).clear();
				driver.findElement(By.name("j_username")).sendKeys(email);
				driver.findElement(By.name("j_password")).clear();
				driver.findElement(By.name("j_password")).sendKeys(password);
				driver.findElement(By.xpath("//input[@value='Login']")).click();
			}
			Assert.assertTrue("Not logged in!", driver.findElement(By.linkText(first_name)) != null);

			driver.findElement(By.linkText(first_name)).click();
			changePassword(password, newpassword, confnewpsswd);
			Assert.assertTrue("Not logged out!", driver.findElement(By.linkText("Login")) != null);

			driver.findElement(By.cssSelector("span")).click();
			driver.findElement(By.name("j_username")).clear();
			driver.findElement(By.name("j_username")).sendKeys(email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(newpassword);
			driver.findElement(By.xpath("//input[@value='Login']")).click();
			Assert.assertTrue("Not logged in!", driver.findElement(By.linkText(first_name)) != null);

			driver.findElement(By.linkText(first_name)).click();
			changePassword(newpassword, password, password);
			driver.findElement(By.cssSelector("a > span")).click();
			Assert.assertTrue("Not logged out!", driver.findElement(By.linkText("Login")) != null);
		} finally {
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + Utils.escapeString(email) + "';");
			tearDown();
		}
	}

	private void setUp() throws Exception {
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

	private void changePassword(String password, String newpassword, String confnewpsswd) {
		driver.findElement(By.linkText("Change Password")).click();
		driver.findElement(By.id("currentPassword")).clear();
		driver.findElement(By.id("currentPassword")).sendKeys(password);
		driver.findElement(By.id("newPassword")).clear();
		driver.findElement(By.id("newPassword")).sendKeys(newpassword);
		driver.findElement(By.id("newPasswordConfirm")).clear();
		driver.findElement(By.id("newPasswordConfirm")).sendKeys(confnewpsswd);
		driver.findElement(By.cssSelector("input.medium.red")).click();
		driver.findElement(By.cssSelector("a > span")).click();
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
