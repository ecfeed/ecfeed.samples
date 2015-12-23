package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.RegisterForm;
import pages.patterns.PageObject;

public class RegisterPage extends PageObject{

	public static final String registrationFormId = "registrationForm";

	@FindBy(how = How.ID, using = registrationFormId)
	private WebElement fRegisterForm;
	private RegisterForm fRegisterFormElement;

	public enum ErrorElements{
		EMAIL_IN_USE("address is already in use"), EMAIL_INVALID("address is invalid"), EMAIL_EMPTY("enter your email address"), FIRSTNAME_EMPTY(
				"Please enter your first name"), FIRSTNAME_INVALID(""), LASTNAME_INVALID(""), LASTNAME_EMPTY("Please enter your last name"), PASSWORD_INVALID(
				"Password must be alphanumeric"), PASSWORD_EMPTY("Please enter a valid password"), PASSWORD_CONFIRM_EMPTY(
				"Please confirm your password"), PASSWORD_CONFIRM_NOT_MATCHING("Passwords didn't match");

		private String message;

		ErrorElements(String error){
			message = error;
		}

		public String getMessage(){
			return message;
		}
	}

	public RegisterPage(WebDriver driver){
		super(driver, tools.PageAddress.REGISTER);
		initialiseElements();
	}

	public boolean isErrorPresent(ErrorElements error){
		return pageContains(error.getMessage());
	}

	public void fillRegisterForm(String email, String first, String last, String password, String password_confirm){
		fRegisterFormElement.fillForm(email, first, last, password, password_confirm);

	}

	public MainPage registerExpectSuccess(){
		fRegisterFormElement.submit();
		return new MainPage(fDriver);
	}

	public RegisterPage registerExpectFailure(){
		fRegisterFormElement.submit();
		initialiseElements();
		return this;
	}

	public RegisterForm getRegisterForm(){
		return fRegisterFormElement;
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fDriver, this);
		fRegisterFormElement = new RegisterForm(fDriver, fRegisterForm);
	}

}
