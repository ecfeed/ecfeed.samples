package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import tools.PageAddress;
import tools.Utils;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestLogin extends TestUserData {

	public TestLogin(){
		baseUrl = PageAddress.Login;
	}

  	/*
  	 * Tests for success.
  	 */
	@Test
	public void testLoginSuccess(String email, String password, String input_email, String input_password) throws Exception {
		String escaped_email = Utils.escapeString(email);
		String escaped_password = Utils.escapeString(password);
		try {
			setUp();
			driver.get(baseUrl);
			
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10110;");
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					escaped_email +"','vname','vname','" + escaped_password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + escaped_email + "',NULL,NULL)");

			login(input_email, input_password);

			if ((email.equals(input_email) && password.equals(input_password))) {
				Assert.assertTrue(isElementPresent(By.linkText("Logout")));
			} else{
				Assert.assertTrue("Login failed", isElementPresent(By.linkText("Login")));
			}
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}

	/*
	 * Test for email field warnings.

	 */
	@Test
	public void testLoginEmail(String email, String input_email, boolean expected_result) throws Exception {
		String escaped_email = Utils.escapeString(email);
		String password = "mypassword";
		try {
			setUp();
			driver.get(baseUrl);

			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10110;");
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					escaped_email +"','vname','vname','" + password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + escaped_email + "',NULL,NULL)");

			login(input_email, password);

			if (expected_result && email.equals(input_email)) {
				Assert.assertTrue( isElementPresent(By.linkText("Logout")));
			} else if (expected_result){
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			} else {
				Assert.assertTrue("Login failed", isElementPresent(By.linkText("Login")));
			}
		} finally {
			cleanUpAfterTest(email);
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
		String escaped_password = Utils.escapeString(password);
		String email = "email@mail.com";
		try {
			setUp();
			driver.get(baseUrl);

			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=10110;");
			connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(10110,10110,'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
					email +"','vname','vname','" + escaped_password +"{10110}',FALSE,NULL,TRUE,TRUE,NULL,'" + email + "',NULL,NULL)");

			login(email, input_password);

			if (expected_result) {
				Assert.assertTrue(isElementPresent(By.linkText("Logout")));
			} else {
				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.LoginNotFound + "')]")));
			}
		} finally {
			cleanUpAfterTest(email);
			tearDown();
		}
	}

}
