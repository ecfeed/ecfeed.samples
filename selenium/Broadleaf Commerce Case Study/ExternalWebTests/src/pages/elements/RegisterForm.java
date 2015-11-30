package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.patterns.AbstractWebElementTypes;
import pages.elements.patterns.ButtonElement;
import pages.elements.patterns.EditableTextElement;
import pages.elements.patterns.FormElement;

public class RegisterForm extends FormElement{

	public final static String EMAIL_ADDRESS_TEXT = "customer.emailAddress";
	public final static String FIRST_NAME_TEXT = "customer.firstName";
	public final static String LAST_NAME_TEXT = "customer.lastName";
	public final static String PASSWORD_TEXT = "password";
	public final static String PASSWORD_CONFIRM_TEXT = "passwordConfirm";
	public final static String REGISTER_BUTTON = "register_button";

	@FindBy(how = How.ID, using = EMAIL_ADDRESS_TEXT)
	private WebElement emailText;
	@FindBy(how = How.ID, using = FIRST_NAME_TEXT)
	private WebElement firstNameText;
	@FindBy(how = How.ID, using = LAST_NAME_TEXT)
	private WebElement lastNameText;
	@FindBy(how = How.ID, using = PASSWORD_TEXT)
	private WebElement passwordText;
	@FindBy(how = How.ID, using = PASSWORD_CONFIRM_TEXT)
	private WebElement passwordConfirmText;
	@FindBy(how = How.CLASS_NAME, using = REGISTER_BUTTON)
	private WebElement registerButton;

	public RegisterForm(WebDriver driver, WebElement element){
		super(driver, element);
		initialiseElements();
	}

	@Override
	public void submit(){
		((ButtonElement)getElement(REGISTER_BUTTON, AbstractWebElementTypes.BUTTON)).click();
	}

	public void fillForm(String email, String first, String last, String pass, String password_confirm){
		fillTextField(EMAIL_ADDRESS_TEXT, email);
		fillTextField(FIRST_NAME_TEXT, first);
		fillTextField(LAST_NAME_TEXT, last);
		fillTextField(PASSWORD_TEXT, pass);
		fillTextField(PASSWORD_CONFIRM_TEXT, password_confirm);
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fElementLocatorFactory, this);
		fElements.put(EMAIL_ADDRESS_TEXT, new EditableTextElement(emailText));
		fElements.put(FIRST_NAME_TEXT, new EditableTextElement(firstNameText));
		fElements.put(LAST_NAME_TEXT, new EditableTextElement(lastNameText));
		fElements.put(PASSWORD_TEXT, new EditableTextElement(passwordText));
		fElements.put(PASSWORD_CONFIRM_TEXT, new EditableTextElement(passwordConfirmText));
		fElements.put(REGISTER_BUTTON, new ButtonElement(registerButton));
	}

}
