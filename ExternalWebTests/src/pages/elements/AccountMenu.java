package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.patterns.ButtonElement;
import pages.elements.patterns.MenuElement;
import pages.elements.patterns.TextElement;

public abstract class AccountMenu extends MenuElement{
	
	public final static String CART_LINK = "modalcart";
	public final static String CART_ITEM_COUNT_TEXT = "headerCartItemsCount";
	
	@FindBy(how = How.LINK_TEXT, using = CART_LINK)
	private WebElement cartButton;
	
	@FindBy(how = How.CLASS_NAME, using = CART_ITEM_COUNT_TEXT)
	private WebElement cartItemCount;

	public AccountMenu(WebElement element, WebDriver driver){
		super(element, driver);
		initialiseElements();
	}
	
	public int getCartItemCount(){
		try{
			return Integer.parseInt(((TextElement)fElements.get(CART_ITEM_COUNT_TEXT)).getText());
		} catch (NumberFormatException e) {
			throw new Error("Cart item count not displayed.");
		}
	}
	
	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fElementLocatorFactory, this);
		fElements.put(CART_LINK, new ButtonElement(cartButton));
		fElements.put(CART_ITEM_COUNT_TEXT, new ButtonElement(cartItemCount));
	}
	
	public abstract boolean isLoggedIn();

}
