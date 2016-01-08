package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import tools.PageAddress;

import com.testify.ecfeed.junit.StaticRunner;
import com.testify.ecfeed.junit.annotations.EcModel;
import com.testify.ecfeed.junit.annotations.expected;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestAccountInformationChange extends UserDataTest{
	
	public TestAccountInformationChange(){
		fBaseUrl = PageAddress.ACCOUNT_INFO;
	}
	
	@Test
	public void testInitialFill(String email, String name, String lastName) throws Exception {
		try{
			setUp();
			deleteAllCustomers();
			insertCustomer(email, "password", name, lastName);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			fDriver.get(fBaseUrl);
			
			Assert.assertTrue("email address doesn't match.", fDriver.findElement(By.id("emailAddress")).getAttribute("value").equals(email));
			Assert.assertTrue("first name doesn't match.", fDriver.findElement(By.id("firstName")).getAttribute("value").equals(name));
			Assert.assertTrue("last name doesn't match.", fDriver.findElement(By.id("lastName")).getAttribute("value").equals(lastName));

			fDriver.findElement(By.linkText("Logout")).click();
		} finally{
			deleteCustomerSafe(email);
			tearDown();
		}
	}
	
	@Test
	public void testEmailChange(String email, String newEmail, boolean valid_data) throws Exception{
		try{
			String name = "Firstname";
			setUp();
			deleteAllCustomers();
			insertCustomer(email, "password", name, "LastName");
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			fDriver.get(fBaseUrl);	
			fDriver.findElement(By.id("emailAddress")).clear();
			fDriver.findElement(By.id("emailAddress")).sendKeys(newEmail);
			fDriver.findElement(By.cssSelector("input.medium.red")).click();
			fDriver.get(fBaseUrl);
			if(valid_data){
				Assert.assertTrue("Email address doesn't match.", fDriver.findElement(By.id("emailAddress")).getAttribute("value").equals(newEmail));
			} else{
				Assert.assertTrue("Email address doesn't match.", fDriver.findElement(By.id("emailAddress")).getAttribute("value").equals(email));
			}
		} finally{
			deleteCustomerSafe(email);
			tearDown();
		}
	}
	
	@Test
	public void testFirstNameChange(String name, String newName, @expected boolean valid_data) throws Exception {
		String email = "standard.email@address.com";
		try{
			setUp();
			deleteAllCustomers();
			insertCustomer(email, "password", name, "LastName");
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			fDriver.get(fBaseUrl);	
			
			fDriver.findElement(By.id("firstName")).clear();
			fDriver.findElement(By.id("firstName")).sendKeys(newName);
			fDriver.findElement(By.cssSelector("input.medium.red")).click();
			
			fDriver.get(fBaseUrl);
			if(valid_data){
				Assert.assertTrue("first name doesn't match.", fDriver.findElement(By.id("firstName")).getAttribute("value").equals(newName));
			} else{
				Assert.assertTrue("first name doesn't match.", fDriver.findElement(By.id("firstName")).getAttribute("value").equals(name));
			}
		} finally{
			deleteCustomerSafe(email);
			tearDown();
		}
	}
	
	@Test
	public void testLastNameChange(String name, String newName, @expected boolean valid_data) throws Exception {
		String email = "standard.email@address.com";
		String firstName = "Firstname";
		try{
			setUp();
			deleteAllCustomers();
			insertCustomer(email, "password", firstName, name);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(firstName)));
			fDriver.get(fBaseUrl);	
			
			fDriver.findElement(By.id("lastName")).clear();
			fDriver.findElement(By.id("lastName")).sendKeys(newName);
			fDriver.findElement(By.cssSelector("input.medium.red")).click();
			
			fDriver.get(fBaseUrl);
			if(valid_data){
				Assert.assertTrue("last name doesn't match.", fDriver.findElement(By.id("lastName")).getAttribute("value").equals(newName));
			} else{
				Assert.assertTrue("last name doesn't match.", fDriver.findElement(By.id("lastName")).getAttribute("value").equals(name));
			}
		} finally{
			deleteCustomerSafe(email);
			tearDown();
		}
	}
	

	@Test
	public void testAccountInfoChangeSuccess(String email, String newEmail, String name, String newName, String lastName, String newLastName, boolean valid_data) throws Exception {	
		try{
			setUp();
			deleteAllCustomers();
			insertCustomer(email, "password", name, lastName);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			fDriver.get(fBaseUrl);	
			
			fDriver.findElement(By.id("emailAddress")).clear();
			fDriver.findElement(By.id("emailAddress")).sendKeys(newEmail);
			fDriver.findElement(By.id("firstName")).clear();
			fDriver.findElement(By.id("firstName")).sendKeys(newName);
			fDriver.findElement(By.id("lastName")).clear();
			fDriver.findElement(By.id("lastName")).sendKeys(newLastName);

			fDriver.findElement(By.cssSelector("input.medium.red")).click();
			
			fDriver.get(fBaseUrl);
			if(valid_data){
				Assert.assertTrue("email address doesn't match.", fDriver.findElement(By.id("emailAddress")).getAttribute("value").equals(newEmail));
				Assert.assertTrue("first name doesn't match.", fDriver.findElement(By.id("firstName")).getAttribute("value").equals(newName));
				Assert.assertTrue("last name doesn't match.", fDriver.findElement(By.id("lastName")).getAttribute("value").equals(newLastName));
			} else{
				Assert.assertTrue("email address doesn't match.", fDriver.findElement(By.id("emailAddress")).getAttribute("value").equals(email));
				Assert.assertTrue("first name doesn't match.", fDriver.findElement(By.id("firstName")).getAttribute("value").equals(name));
				Assert.assertTrue("last name doesn't match.", fDriver.findElement(By.id("lastName")).getAttribute("value").equals(lastName));
			}
		} finally{
			deleteCustomerSafe(email);
			tearDown();
		}
	}
	
	@Test
	public void testSuccessNotification(String email, String newEmail, String name, String newName, String lastName, String newLastName, boolean valid_data) throws Exception {	
		try{
			setUp();
			deleteAllCustomers();
			insertCustomer(email, "password", name, lastName);
			
			login(email, "password");
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			fDriver.get(fBaseUrl);	
			
			fDriver.findElement(By.id("emailAddress")).clear();
			fDriver.findElement(By.id("emailAddress")).sendKeys(newEmail);
			fDriver.findElement(By.id("firstName")).clear();
			fDriver.findElement(By.id("firstName")).sendKeys(newName);
			fDriver.findElement(By.id("lastName")).clear();
			fDriver.findElement(By.id("lastName")).sendKeys(newLastName);

			fDriver.findElement(By.cssSelector("input.medium.red")).click();

			if(valid_data){
				Assert.assertTrue(isElementPresent(By.className("success")));
			} else{
				Assert.assertTrue(!isElementPresent(By.className("success")));
			}
		} finally{
			deleteCustomerSafe(email);
			tearDown();
		}
	}
	
}
