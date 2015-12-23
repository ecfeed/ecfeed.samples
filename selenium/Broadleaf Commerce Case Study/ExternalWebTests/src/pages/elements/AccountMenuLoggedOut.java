package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.LoginPage;
import pages.RegisterPage;
import pages.elements.patterns.ButtonElement;

public class AccountMenuLoggedOut extends AccountMenu{

	public final static String LOGIN_LINK = "Login";
	public final static String REGISTER_LINK = "Register";

	@FindBy(how = How.LINK_TEXT, using = LOGIN_LINK)
	private WebElement fLoginButton;

	@FindBy(how = How.LINK_TEXT, using = REGISTER_LINK)
	private WebElement fRegisterButton;

	public AccountMenuLoggedOut(WebElement element, WebDriver driver){
		super(element, driver);

		initialiseElements();
	}

	@Override
	public void initialiseElements(){
		PageFactory.initElements(fElementLocatorFactory, this);
		fElements.put(LOGIN_LINK, new ButtonElement(fLoginButton));
		fElements.put(REGISTER_LINK, new ButtonElement(fRegisterButton));
	}

	public RegisterPage navigateRegister(){
		((ButtonElement)fElements.get(REGISTER_LINK)).click();
		return new RegisterPage(fDriver);
	}

	public LoginPage navigateLogin(){
		((ButtonElement)fElements.get(LOGIN_LINK)).click();
		return new LoginPage(fDriver);
	}

	@Override
	public boolean isLoggedIn(){
		try{
			fLoginButton.getLocation();
			return false;
		} catch(Exception e){
			return true;
		}
	}
}
