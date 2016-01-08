package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.AccountPage;
import pages.MainPage;
import pages.elements.patterns.ButtonElement;

public class AccountMenuLoggedIn extends AccountMenu{

	public final static String ACCOUNT_LINK = "my-account";
	public final static String LOGOUT_LINK = "Logout";

	@FindBy(how = How.LINK_TEXT, using = LOGOUT_LINK)
	private WebElement fLogoutButton;

	@FindBy(how = How.CLASS_NAME, using = ACCOUNT_LINK)
	private WebElement fAccountButton;

	public AccountMenuLoggedIn(WebElement element, WebDriver driver){
		super(element, driver);

		initialiseElements();
	}

	@Override
	public void initialiseElements(){
		PageFactory.initElements(fElementLocatorFactory, this);
		fElements.put(ACCOUNT_LINK, new ButtonElement(fAccountButton));
		fElements.put(LOGOUT_LINK, new ButtonElement(fLogoutButton));
	}

	public String getUserName(){
		return fAccountButton.getText();
	}

	public MainPage navigateLogout(){
		((ButtonElement)fElements.get(LOGOUT_LINK)).click();
		return new MainPage(fDriver);
	}

	public AccountPage navigateAccount(){
		((ButtonElement)fElements.get(ACCOUNT_LINK)).click();
		return new AccountPage(fDriver);
	}

	@Override
	public boolean isLoggedIn(){
		try{
			fLogoutButton.getLocation();
			return true;
		} catch(Exception e){
			return false;
		}
	}

}
