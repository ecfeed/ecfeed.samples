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
import org.openqa.selenium.firefox.FirefoxDriver;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.*;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestComplexLoginAndChangePassword{
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	private boolean setUp = false;

	private String login_taken_error = "email address is already in use";

	@Before
	public void setUp() throws Exception{
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8080";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		setUp = true;
	}


	private void changePassword(String password, String newpassword, String confnewpsswd){
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

	@Test
	public void testRegisterBasic(String email, String first_name, String last_name, String password, String confpsswd, String newpassword,
			String confnewpsswd, boolean valid_data) throws Exception{
		if(!setUp){
			try{
				setUp();
			} catch(Exception e){
				throw new AssertionError("Failed to initialize Selenium driver");
			}
		}
		
		driver.get(baseUrl + "/");
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

		boolean email_taken = isElementPresent(By.xpath("//[contains(.,'" +login_taken_error+ "')]"));
		if(email_taken){
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

		Assert.assertTrue("Not logged in!", driver.findElement(By.linkText(first_name)) != null); // revert
																									// password
		// change and logout
		driver.findElement(By.linkText(first_name)).click();
		changePassword(newpassword, password, password);
		driver.findElement(By.cssSelector("a > span")).click();
		Assert.assertTrue("Not logged out!", driver.findElement(By.linkText("Login")) != null);

	}

	@After
	public void tearDown() throws Exception{
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if(!"".equals(verificationErrorString)){
			fail(verificationErrorString);
		}
		setUp = false;
	}

	private boolean isElementPresent(By by){
		try{
			driver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}

	private boolean isAlertPresent(){
		try{
			driver.switchTo().alert();
			return true;
		} catch(NoAlertPresentException e){
			return false;
		}
	}

	private String closeAlertAndGetItsText(){
		try{
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if(acceptNextAlert){
				alert.accept();
			} else{
				alert.dismiss();
			}
			return alertText;
		} finally{
			acceptNextAlert = true;
		}
	}
}
