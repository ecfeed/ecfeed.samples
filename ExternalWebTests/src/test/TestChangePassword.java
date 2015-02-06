package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import tools.PageAddress;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestChangePassword extends UserDataTest {

	public TestChangePassword(){
		baseUrl = PageAddress.LOGIN;
	}
	
	@Test
	public void testChangePasswordSuccess(String password, String currentpsswd, String newpassword, String confnewpsswd) throws Exception{
		String email = "standard.email@address.com";
		String first_name = "Firstname";

		try{
			setUp();
			tryLoginAndChangePassword(email, first_name, password, currentpsswd, newpassword, confnewpsswd);
			driver.findElement(By.linkText("Logout")).click();
			Assert.assertTrue("Not logged out!", isElementPresent(By.linkText("Login")));

			login(email, newpassword);

			if(((password.equals(currentpsswd) && newpassword.equals(confnewpsswd)) && !newpassword.equals(currentpsswd))){
				Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(first_name)));
			} else{
				Assert.assertFalse("Shouldn't be logged in!", isElementPresent(By.linkText(first_name)));
			}
		} finally{
			cleanUpUserTable(email);
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
			cleanUpUserTable(email);
			tearDown();
		}
	
	}
	
	private void tryLoginAndChangePassword(String email, String first_name, String password, String currentpsswd, String newpassword,
			String confnewpsswd){
		insertCustomer(email, password, first_name, "LastName");
		
		login(email, password);	
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

}
