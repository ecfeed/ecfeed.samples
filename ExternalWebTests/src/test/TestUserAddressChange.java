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

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;
import com.testify.ecfeed.runner.annotations.expected;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestUserAddressChange extends UserDataTest{

	public TestUserAddressChange(){
		baseUrl = PageAddress.ADDRESS_INFO;
	}
	
	@Test
	public void testAddAddress(String firstName,String lastName, String phone, String addr1, String addr2, String city, 
											State state, String postal, String addrName, boolean isDefault, @expected boolean valid_data) throws Exception{
		String email = "standard.email@address.com";
		String password = "samplePassword";
		String name = "Firstname";
		String surname = "Surname";
		long user_id = 0;
		
		try{
			setUp();
			user_id = insertCustomer(email, password, name, surname);
			login(email, password);
			
			Assert.assertTrue("Not logged in!", isElementPresent(By.linkText(name)));
			driver.get(baseUrl);	
			
			 driver.findElement(By.id("address.firstName")).clear();
			 driver.findElement(By.id("address.firstName")).sendKeys(firstName);
			 driver.findElement(By.id("address.lastName")).clear();
			 driver.findElement(By.id("address.lastName")).sendKeys(lastName);
			 driver.findElement(By.id("address.phonePrimary")).clear();
			 driver.findElement(By.id("address.phonePrimary")).sendKeys(phone);
			 driver.findElement(By.id("address.addressLine1")).clear();
			 driver.findElement(By.id("address.addressLine1")).sendKeys(addr1);
			 driver.findElement(By.id("address.addressLine2")).clear();
			 driver.findElement(By.id("address.addressLine2")).sendKeys(addr2);
			 driver.findElement(By.id("address.city")).clear();
			 driver.findElement(By.id("address.city")).sendKeys(city);
			 new Select(driver.findElement(By.id("state"))).selectByVisibleText(state.toString());
			 driver.findElement(By.id("address.postalCode")).clear();
			 driver.findElement(By.id("address.postalCode")).sendKeys(postal);
			 driver.findElement(By.id("addressName")).clear();
			 driver.findElement(By.id("addressName")).sendKeys(addrName);
			 driver.findElement(By.id("address.default1")).click();
			 driver.findElement(By.cssSelector("input.medium.red")).click();
			
			driver.get(baseUrl);
			boolean addressPresent = false;
			List<WebElement> dropDownList = driver.findElements(By.cssSelector("option"));
			for(WebElement element : dropDownList){
				if(element.getText().equals(addrName + " (" + addr1 + ")")){
					addressPresent = true;
					break;
				}

			}
			
			if(valid_data){
				Assert.assertTrue("Cannot find address!", addressPresent);

				new Select(driver.findElement(By.cssSelector("select"))).selectByVisibleText(addrName + " (" + addr1 + ")");

				Assert.assertTrue("First name doesn't match",
						driver.findElement(By.id("address.firstName")).getAttribute("value").equals(firstName));
				Assert.assertTrue("Last name doesn't match",
						driver.findElement(By.id("address.lastName")).getAttribute("value").equals(lastName));
				Assert.assertTrue("Phone doesn't match",
						driver.findElement(By.id("address.phonePrimary")).getAttribute("value").equals(phone));
				Assert.assertTrue("Address 1 doesn't match", driver.findElement(By.id("address.addressLine1")).getAttribute("value")
						.equals(addr1));
				Assert.assertTrue("Address 2 doesn't match", driver.findElement(By.id("address.addressLine2")).getAttribute("value")
						.equals(addr2));
				Assert.assertTrue("City doesn't match", driver.findElement(By.id("address.city")).getAttribute("value").equals(city));
				Assert.assertTrue("Postal doesn't match",
						driver.findElement(By.id("address.postalCode")).getAttribute("value").equals(postal));
				if(state.equals(State.NONE)){
					Assert.assertTrue("State doesn't match", driver.findElement(By.id("state")).getAttribute("value").equals(""));
				} else{
					Assert.assertTrue("State doesn't match",
							driver.findElement(By.id("state")).getAttribute("value").equals(state.toString()));
				}

			} else{
				Assert.assertFalse("Address should fail to be created!", addressPresent);
			}
		}
		finally{
			cleanUpUser(user_id);
			tearDown();
		}
		
	}


}
