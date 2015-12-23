package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.MainPage;
import pages.MerchandisePage;
import pages.SaucesPage;
import pages.elements.patterns.ButtonElement;
import pages.elements.patterns.MenuElement;

public class MainMenu extends MenuElement {
	
	public static final String HOME_LINK = "Home";
	public static final String HOT_SAUCES_LINK = "Hot sauce";
	public static final String MERCHANDISE_LINK = "Merchandise";
	public static final String CLEARANCE_LINK = "Clearance";
	public static final String NEW_TO_HOT_SAUCE_LINK = "New to hot sauce?";
	public static final String FAQ_LINK = "FAQs";
	
	@FindBy(how = How.LINK_TEXT, using = HOME_LINK)
	private WebElement fHomeLink;
	
	@FindBy(how = How.LINK_TEXT, using = HOT_SAUCES_LINK)
	private WebElement fHotSaucesLink;
	
	@FindBy(how = How.LINK_TEXT, using = CLEARANCE_LINK)
	private WebElement fClearanceLink;
	
	@FindBy(how = How.LINK_TEXT, using = MERCHANDISE_LINK)
	private WebElement fMerchandiseLink;
	
	@FindBy(how = How.LINK_TEXT, using = FAQ_LINK)
	private WebElement fFaqLink;

	public MainMenu(WebElement element, WebDriver driver){
		super(element, driver);
		initialiseElements();
	}
	
	public MainPage goHomePage(){
		getChildElement(HOME_LINK).click();
		return new MainPage(fDriver);
	}
	
	public SaucesPage goSaucesPage(){
		getChildElement(HOT_SAUCES_LINK).click();
		return new SaucesPage(fDriver);
	}
	
	public MerchandisePage goMerchandisePage(){
		getChildElement(MERCHANDISE_LINK).click();
		return new MerchandisePage(fDriver);
	}
	
	@Override
	public ButtonElement getChildElement(String element){
		return (ButtonElement)super.getChildElement(element);
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fElementLocatorFactory, this);
		fElements.put(HOME_LINK, new ButtonElement(fHomeLink));
		fElements.put(MERCHANDISE_LINK, new ButtonElement(fMerchandiseLink));
		fElements.put(CLEARANCE_LINK, new ButtonElement(fClearanceLink));
		fElements.put(NEW_TO_HOT_SAUCE_LINK, new ButtonElement(fHotSaucesLink));
		fElements.put(FAQ_LINK, new ButtonElement(fFaqLink));
	}

}
