package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import tools.PageAddress;
import tools.DBUtils;

import com.testify.ecfeed.junit.StaticRunner;
import com.testify.ecfeed.junit.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestLogin extends UserDataTest {

	public TestLogin(){
		baseUrl = PageAddress.LOGIN;
	}

  	/*
  	 * Tests for success.
  	 */
	@Test
	public void testLoginSuccess(String email, String password, String input_email, String input_password) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);
			insertCustomer(email, password, "FirstName", "LastName");

			login(input_email, input_password);

			if ((email.equals(input_email) && password.equals(input_password))) {
				Assert.assertTrue(isElementPresent(By.linkText("Logout")));
			} else{
				Assert.assertTrue("Login failed", isElementPresent(By.linkText("Login")));
			}
		} finally {
			cleanUpUserTableSafe(email);
			tearDown();
		}
	}

	/*
	 * Test for email field warnings.

	 */
	@Test
	public void testLoginEmail(String email, String input_email, boolean expected_result) throws Exception {
		String password = "mypassword";
		try {
			setUp();
			driver.get(baseUrl);
			insertCustomer(email, password, "FirstName", "LastName");

			login(input_email, password);

			if (expected_result && email.equals(input_email)) {
				Assert.assertTrue( isElementPresent(By.linkText("Logout")));
			} else if (expected_result){
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			} else {
				Assert.assertTrue("Login failed", isElementPresent(By.linkText("Login")));
			}
		} finally {
			cleanUpUserTableSafe(email);
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
		String escaped_password = DBUtils.escapeString(password);
		String email = "email@mail.com";
		try {
			setUp();
			insertCustomer(email, escaped_password, "firstName", "lastName");
			
			driver.get(baseUrl);
			login(email, input_password);

			if (expected_result) {
				Assert.assertTrue(isElementPresent(By.linkText("Logout")));
			} else {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			}
		} finally {
			cleanUpUserTableSafe(email);
			tearDown();
		}
	}

}
