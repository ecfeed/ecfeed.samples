package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import test.enums.State;
import tools.PageAddress;

import com.testify.ecfeed.junit.StaticRunner;
import com.testify.ecfeed.junit.annotations.EcModel;
import com.testify.ecfeed.junit.annotations.TestSuites;
import com.testify.ecfeed.junit.annotations.expected;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestUserAddressChange extends UserDataTest{

	public TestUserAddressChange(){
		fBaseUrl = PageAddress.ADDRESS_INFO;
	}
	
	@Test
	@TestSuites("Failed")
	public void testAddAddress(String firstName,String lastName, String phone, String addr1, String addr2, String city, 
											State state, String postal, String addrName, boolean isDefault, @expected boolean valid_data) throws Exception{
		String email = "standard.email@address.com";
		String password = "samplePassword";
		String name = "Firstname";
		String surname = "Surname";
		long user_id = 0;
		
		try{
			setUp();
			cleanUpUserTable();
			user_id = insertCustomer(email, password, name, surname);
			login(email, password);
			
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			fDriver.get(fBaseUrl);	
			
			 fDriver.findElement(By.id("address.firstName")).clear();
			 fDriver.findElement(By.id("address.firstName")).sendKeys(firstName);
			 fDriver.findElement(By.id("address.lastName")).clear();
			 fDriver.findElement(By.id("address.lastName")).sendKeys(lastName);
			 fDriver.findElement(By.id("address.phonePrimary")).clear();
			 fDriver.findElement(By.id("address.phonePrimary")).sendKeys(phone);
			 fDriver.findElement(By.id("address.addressLine1")).clear();
			 fDriver.findElement(By.id("address.addressLine1")).sendKeys(addr1);
			 fDriver.findElement(By.id("address.addressLine2")).clear();
			 fDriver.findElement(By.id("address.addressLine2")).sendKeys(addr2);
			 fDriver.findElement(By.id("address.city")).clear();
			 fDriver.findElement(By.id("address.city")).sendKeys(city);
			 new Select(fDriver.findElement(By.id("state"))).selectByVisibleText(state.toString());
			 fDriver.findElement(By.id("address.postalCode")).clear();
			 fDriver.findElement(By.id("address.postalCode")).sendKeys(postal);
			 fDriver.findElement(By.id("addressName")).clear();
			 fDriver.findElement(By.id("addressName")).sendKeys(addrName);
			 fDriver.findElement(By.id("address.default1")).click();
			 fDriver.findElement(By.cssSelector("input.medium.red")).click();
			
			fDriver.get(fBaseUrl);
			boolean addressPresent = false;
			List<WebElement> dropDownList = fDriver.findElements(By.cssSelector("option"));
			for(WebElement element : dropDownList){
				if(element.getText().equals(addrName + " (" + addr1 + ")")){
					addressPresent = true;
					break;
				}

			}
			
			if(valid_data){
				Assert.assertTrue("Cannot find address!", addressPresent);

				new Select(fDriver.findElement(By.cssSelector("select"))).selectByVisibleText(addrName + " (" + addr1 + ")");

				Assert.assertTrue("First name doesn't match",
						fDriver.findElement(By.id("address.firstName")).getAttribute("value").equals(firstName));
				Assert.assertTrue("Last name doesn't match",
						fDriver.findElement(By.id("address.lastName")).getAttribute("value").equals(lastName));
				Assert.assertTrue("Phone doesn't match",
						fDriver.findElement(By.id("address.phonePrimary")).getAttribute("value").equals(phone));
				Assert.assertTrue("Address 1 doesn't match", fDriver.findElement(By.id("address.addressLine1")).getAttribute("value")
						.equals(addr1));
				Assert.assertTrue("Address 2 doesn't match", fDriver.findElement(By.id("address.addressLine2")).getAttribute("value")
						.equals(addr2));
				Assert.assertTrue("City doesn't match", fDriver.findElement(By.id("address.city")).getAttribute("value").equals(city));
				Assert.assertTrue("Postal doesn't match",
						fDriver.findElement(By.id("address.postalCode")).getAttribute("value").equals(postal));
				if(state.equals(State.NONE)){
					Assert.assertTrue("State doesn't match", fDriver.findElement(By.id("state")).getAttribute("value").equals(""));
				} else{
					Assert.assertTrue("State doesn't match",
							fDriver.findElement(By.id("state")).getAttribute("value").equals(state.toString()));
				}

			} else{
				Assert.assertFalse("Address should fail to be created!", addressPresent);
			}
		}
		finally{
			cleanUpUserSafe(user_id);
			tearDown();
		}
		
	}


}
