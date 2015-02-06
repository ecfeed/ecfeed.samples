package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.AccountMenu;
import pages.elements.AccountMenuLoggedIn;
import pages.elements.AccountMenuLoggedOut;
import pages.elements.MainMenu;
import pages.elements.SearchBar;
import pages.patterns.PageObject;

public class MainPage extends PageObject{

	public final static String SEARCH_BAR = "q";
	public final static String MAIN_MENU = "id('content')/nav";
	public final static String ACCOUNT_MENU = "id('cart_info')";

	@FindBy(how = How.XPATH, using = MAIN_MENU)
	private WebElement mainMenu;
	@FindBy(how = How.XPATH, using = ACCOUNT_MENU)
	private WebElement accountMenu;
	@FindBy(how = How.NAME, using = SEARCH_BAR)
	private WebElement searchBar;
	protected boolean isLoggedOn;

	public MainPage(WebDriver driver){
		super(driver, tools.PageAddress.BASE);
		get();
		initialiseElements();
	}
	
	public MainPage(WebDriver driver, String url){
		super(driver, url);

		initialiseElements();
	}

	public MainMenu getMainMenu(){
		return (MainMenu)(fElements.get(MAIN_MENU));
	}

	public AccountMenu getAccountMenu(){
		return (AccountMenu)(fElements.get(ACCOUNT_MENU));
	}

	public SearchBar getSearchBar(){
		return (SearchBar)(fElements.get(SEARCH_BAR));
	}

	public RegisterPage navigateRegister(){
		((AccountMenuLoggedOut)getAccountMenu()).navigateRegister();
		return new RegisterPage(fDriver);
	}

	public LoginPage navigateLogin(){
		((AccountMenuLoggedOut)getAccountMenu()).navigateLogin();
		return new LoginPage(fDriver);
	}
	
	public AccountPage navigateAccount(){
		if(isLoggedIn())
			((AccountMenuLoggedIn)getAccountMenu()).navigateAccount();
		return new AccountPage(fDriver);
	}
	
	public SearchPage search(String searchString){
		getSearchBar().fillSearch(searchString);
		getSearchBar().submit();
		
		return new SearchPage(fDriver, searchString);
	}
	
	public boolean isLoggedIn(){
		AccountMenuLoggedIn accMenu = new AccountMenuLoggedIn(accountMenu, fDriver);
		if(accMenu.isLoggedIn()){
			fElements.put(ACCOUNT_MENU, accMenu);
			return isLoggedOn = true;
		}

		AccountMenuLoggedOut accMenu2 = new AccountMenuLoggedOut(accountMenu, fDriver);
		if(!accMenu2.isLoggedIn()){
			fElements.put(ACCOUNT_MENU, accMenu2);
			return isLoggedOn = false;
		}
		return false;
	}

	@Override
	protected void initialiseElements(){
		PageFactory.initElements(fDriver, this);
		isLoggedIn();
		fElements.put(MAIN_MENU, new MainMenu(mainMenu, fDriver));
		fElements.put(SEARCH_BAR, new SearchBar(searchBar, fDriver));
	}

}
