package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.patterns.ButtonElement;
import pages.elements.patterns.FormElement;
import pages.elements.patterns.EditableTextElement;

public class LoginForm extends FormElement{
	
	public final static String USERNAME_TEXT = "j_username";
	public final static String PASSWORD_TEXT = "j_password";
	public final static String LOGIN_BUTTON = "login_button";
	
	@FindBy(how = How.NAME, using = USERNAME_TEXT)
	private WebElement fUsernameText;
	@FindBy(how = How.NAME, using = PASSWORD_TEXT)
	private WebElement fPasswordText;
	@FindBy(how = How.CLASS_NAME, using = LOGIN_BUTTON)
	private WebElement fLoginButton;

	public LoginForm(WebDriver driver, WebElement element){
		super(driver, element);
		initialiseElements();
	}

	@Override
	public void submit(){
		((ButtonElement)(fElements.get(LOGIN_BUTTON))).click();
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fElementLocatorFactory, this);
		fElements.put(USERNAME_TEXT, new EditableTextElement(fUsernameText));
		fElements.put(PASSWORD_TEXT, new EditableTextElement(fPasswordText));
		fElements.put(LOGIN_BUTTON, new ButtonElement(fLoginButton));	
	}

	public void fillUsername(String username){
		((EditableTextElement)fElements.get(USERNAME_TEXT)).clearAndType(username);
	}

	public void fillPassword(String password){
		((EditableTextElement)fElements.get(PASSWORD_TEXT)).clearAndType(password);
	}

}
