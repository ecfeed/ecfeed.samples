package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.LoginForm;

public class LoginPage extends MainPage{
	
	public static final String LOGIN_FORM_ID = "login";
	@FindBy(how = How.ID, using = LOGIN_FORM_ID)
	private WebElement fLoginForm;
	private LoginForm fLoginFormElement;
	
	public LoginPage(WebDriver driver){
		super(driver, tools.PageAddress.LOGIN);
		initialiseElements();
	}
	
	public void fillUsername(String username){
		fLoginFormElement.fillUsername(username);
	}
	
	public void fillPassword(String password){
		fLoginFormElement.fillPassword(password);
	}
	
	public LoginPage loginExpectFailure(){
		fLoginFormElement.submit();
		initialiseElements();
		return this;
	}
	
	public MainPage loginExpectSuccess(){
		fLoginFormElement.submit();
		return new MainPage(fDriver);
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fDriver, this);
		fLoginFormElement = new LoginForm(fDriver, fLoginForm);
	}

}
