package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import tools.PageAddress;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;
import com.testify.ecfeed.runner.annotations.expected;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestAccountInformationChange extends UserDataTest{
	
	public TestAccountInformationChange(){
		baseUrl = PageAddress.ACCOUNT_INFO;
	}
	
	@Test
	public void testInitialFill(String email, String name, String lastName) throws Exception {
		try{
			setUp();
			insertCustomer(email, "password", name, lastName);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			driver.get(baseUrl);
			
			Assert.assertTrue("email address doesn't match.", driver.findElement(By.id("emailAddress")).getAttribute("value").equals(email));
			Assert.assertTrue("first name doesn't match.", driver.findElement(By.id("firstName")).getAttribute("value").equals(name));
			Assert.assertTrue("last name doesn't match.", driver.findElement(By.id("lastName")).getAttribute("value").equals(lastName));

			driver.findElement(By.linkText("Logout")).click();
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}
	
	@Test
	public void testEmailChange(String email, String newEmail, boolean valid_data) throws Exception{
		try{
			String name = "Firstname";
			setUp();
			insertCustomer(email, "password", name, "LastName");
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			driver.get(baseUrl);	
			driver.findElement(By.id("emailAddress")).clear();
			driver.findElement(By.id("emailAddress")).sendKeys(newEmail);
			driver.findElement(By.cssSelector("input.medium.red")).click();
			driver.get(baseUrl);
			if(valid_data){
				Assert.assertTrue("email address doesn't match.", driver.findElement(By.id("emailAddress")).getAttribute("value").equals(newEmail));
			} else{
				Assert.assertTrue("email address doesn't match.", driver.findElement(By.id("emailAddress")).getAttribute("value").equals(email));
			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}
	
	@Test
	public void testFirstNameChange(String name, String newName, @expected boolean valid_data) throws Exception {
		String email = "standard.email@address.com";
		try{
			setUp();
			insertCustomer(email, "password", name, "LastName");
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			driver.get(baseUrl);	
			
			driver.findElement(By.id("firstName")).clear();
			driver.findElement(By.id("firstName")).sendKeys(newName);
			driver.findElement(By.cssSelector("input.medium.red")).click();
			
			driver.get(baseUrl);
			if(valid_data){
				Assert.assertTrue("first name doesn't match.", driver.findElement(By.id("firstName")).getAttribute("value").equals(newName));
			} else{
				Assert.assertTrue("first name doesn't match.", driver.findElement(By.id("firstName")).getAttribute("value").equals(name));
			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}
	
	@Test
	public void testLastNameChange(String name, String newName, @expected boolean valid_data) throws Exception {
		String email = "standard.email@address.com";
		String firstName = "Firstname";
		try{
			setUp();
			insertCustomer(email, "password", firstName, name);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(firstName)));
			driver.get(baseUrl);	
			
			driver.findElement(By.id("lastName")).clear();
			driver.findElement(By.id("lastName")).sendKeys(newName);
			driver.findElement(By.cssSelector("input.medium.red")).click();
			
			driver.get(baseUrl);
			if(valid_data){
				Assert.assertTrue("last name doesn't match.", driver.findElement(By.id("lastName")).getAttribute("value").equals(newName));
			} else{
				Assert.assertTrue("last name doesn't match.", driver.findElement(By.id("lastName")).getAttribute("value").equals(name));
			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}
	

	@Test
	public void testAccountInfoChangeSuccess(String email, String newEmail, String name, String newName, String lastName, String newLastName, boolean valid_data) throws Exception {	
		try{
			setUp();
			insertCustomer(email, "password", name, lastName);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			driver.get(baseUrl);	
			
			driver.findElement(By.id("emailAddress")).clear();
			driver.findElement(By.id("emailAddress")).sendKeys(newEmail);
			driver.findElement(By.id("firstName")).clear();
			driver.findElement(By.id("firstName")).sendKeys(newName);
			driver.findElement(By.id("lastName")).clear();
			driver.findElement(By.id("lastName")).sendKeys(newLastName);

			driver.findElement(By.cssSelector("input.medium.red")).click();
			
			driver.get(baseUrl);
			if(valid_data){
				Assert.assertTrue("email address doesn't match.", driver.findElement(By.id("emailAddress")).getAttribute("value").equals(newEmail));
				Assert.assertTrue("first name doesn't match.", driver.findElement(By.id("firstName")).getAttribute("value").equals(newName));
				Assert.assertTrue("last name doesn't match.", driver.findElement(By.id("lastName")).getAttribute("value").equals(newLastName));
			} else{
				Assert.assertTrue("email address doesn't match.", driver.findElement(By.id("emailAddress")).getAttribute("value").equals(email));
				Assert.assertTrue("first name doesn't match.", driver.findElement(By.id("firstName")).getAttribute("value").equals(name));
				Assert.assertTrue("last name doesn't match.", driver.findElement(By.id("lastName")).getAttribute("value").equals(lastName));
			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}
	
	@Test
	public void testSuccessNotification(String email, String newEmail, String name, String newName, String lastName, String newLastName, boolean valid_data) throws Exception {	
		try{
			setUp();
			insertCustomer(email, "password", name, lastName);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			driver.get(baseUrl);	
			
			driver.findElement(By.id("emailAddress")).clear();
			driver.findElement(By.id("emailAddress")).sendKeys(newEmail);
			driver.findElement(By.id("firstName")).clear();
			driver.findElement(By.id("firstName")).sendKeys(newName);
			driver.findElement(By.id("lastName")).clear();
			driver.findElement(By.id("lastName")).sendKeys(newLastName);

			driver.findElement(By.cssSelector("input.medium.red")).click();

			if(valid_data){
				Assert.assertTrue(isElementPresent(By.className("success")));
			} else{
				Assert.assertTrue(!isElementPresent(By.className("success")));
			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}
	
}
