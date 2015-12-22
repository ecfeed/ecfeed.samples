package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;
import pages.elements.AccountMenuLoggedIn;
import tools.PageAddress;

import com.testify.ecfeed.junit.StaticRunner;
import com.testify.ecfeed.junit.annotations.EcModel;
import com.testify.ecfeed.junit.annotations.TestSuites;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestRegister extends UserDataTest{
	private String registerUrl = PageAddress.REGISTER;
	/*
	 * This tests checks and warnings for email field.
	 */
	//@Test
	public void testRegisterEmail(String email, boolean expected_result) throws Exception{
		try{
			setUp();
			MainPage mainPage = new MainPage(driver);
			RegisterPage registerPage = mainPage.navigateRegister();
			
			registerPage.fillRegisterForm(email, "first", "last", "password", "password");
			
			if(!expected_result){
				registerPage = registerPage.registerExpectFailure();
				registerPage.isLoaded();
			} else {
				mainPage = registerPage.registerExpectSuccess();
				mainPage.isLoaded();
			}
//			if(!expected_result){
//				registerPage = registerPage.registerExpectFailure();
//				registerPage.isLoaded();
//				Assert.assertTrue("Email field error should be thrown", registerPage.fieldHasError(AccessibleElements.EMAIL_FIELD));
//				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.AddressInvalid + "')]"))
//						|| isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.EnterAddress + "')]"))
//						|| isElementPresent(By.linkText("Login")));
//			} else{
//				mainPage = registerPage.registerExpectSuccess();
//				mainPage.isLoaded();
//				Assert.assertTrue((isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.AddressInUse + "')]"))
//						|| (isElementPresent(By.linkText("Logout"))))
//						&& !isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.AddressInvalid + "')]"))
//						&& !isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.EnterAddress + "')]")));
//			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}

	/*
	 * This tests checks and warnings for first name field.
	 */
	//@Test
//	public void testRegisterFirstName(String first_name, boolean expected_result) throws Exception{
//			if(!expected_result){
//				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.EnterFirstName + "')]"))
//						|| driver.getCurrentUrl().equals(registerUrl));
//			} else{
//				Assert.assertTrue((isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.AddressInUse + "')]")) || isElementPresent(By.linkText("Logout"))));
//			}
//	}
//	public void testRegisterLastName(String last_name, boolean expected_result) throws Exception{
//			if(!expected_result){
//				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.EnterLastName + "')]"))
//						|| driver.getCurrentUrl().equals(registerUrl));
//			} else{
//				Assert.assertTrue((isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.AddressInUse + "')]"))
//						|| isElementPresent(By.linkText("Logout"))));
//			}
//	}
//	public void testRegisterPassword(String password, boolean expected_result) throws Exception{
//			if(!expected_result){
//				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.EnterValidPassword + "')]"))
//						|| isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.InvalidPassword + "')]"))
//						|| driver.getCurrentUrl().equals(registerUrl));
//	}
//	public void testRegisterConfirmPassword(String password, String confirm_password, boolean expected_result) throws Exception{
//
//			if(!expected_result){
//				Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.PasswordsMustMatch + "')]"))
//						|| isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.ConfirmPasswod + "')]"))
//						|| driver.getCurrentUrl().equals(registerUrl));
//			} else{
//				Assert.assertTrue((isElementPresent(By.xpath("//*[contains(.,'" + ErrorMessage.AddressInUse + "')]")) || isElementPresent(By.linkText("Logout"))));
//			}
//	}

	/*
	 * This test checks registering in very simple binary manner - either
	 * success or failure. It doesn't check if proper warnings etc. appear.
	 */
	@Test
	@TestSuites("Testing")
	public void testRegisterUser(String email, String first_name, String last_name, String password, String password_confirm,
			boolean valid_data) throws Exception{
		try{
			setUp();
			MainPage mainPage = new MainPage(driver);
			mainPage.get();
			RegisterPage registerPage = mainPage.navigateRegister();
			registerPage.isLoaded();
			registerPage.fillRegisterForm(email, first_name, last_name, password, password_confirm);

			if(valid_data){
				mainPage = registerPage.registerExpectSuccess();
				mainPage.isLoaded();
				if(mainPage.isLoggedIn()){
					mainPage = ((AccountMenuLoggedIn)(mainPage.getAccountMenu())).navigateLogout();
				}
				mainPage = loginExpectSuccess(email, password, (LoginPage)mainPage.navigateLogin());
				Assert.assertTrue("should be successfully logged on.", mainPage.isLoggedIn());
				mainPage = ((AccountMenuLoggedIn)(mainPage.getAccountMenu())).navigateLogout();

			} else{
				registerPage.registerExpectFailure();
				Assert.assertTrue("Registration should have failed", (registerPage.getRegisterForm().hasError()) || registerPage.isLoaded());
				mainPage = loginExpectFailure(email, password, mainPage.navigateLogin());
				Assert.assertTrue("shouldn't be successfully logged on.", mainPage.isLoggedIn());
			}
		} finally{
			cleanUpUserTable(email);
			tearDown();
		}
	}

}
