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
	private WebElement loginForm;
	private LoginForm loginFormElement;
	
	public LoginPage(WebDriver driver){
		super(driver, tools.PageAddress.LOGIN);
		initialiseElements();
	}
	
	public void fillUsername(String username){
		loginFormElement.fillUsername(username);
	}
	
	public void fillPassword(String password){
		loginFormElement.fillPassword(password);
	}
	
	public LoginPage loginExpectFailure(){
		loginFormElement.submit();
		initialiseElements();
		return this;
	}
	
	public MainPage loginExpectSuccess(){
		loginFormElement.submit();
		return new MainPage(fDriver);
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fDriver, this);
		loginFormElement = new LoginForm(fDriver, loginForm);
	}

}
