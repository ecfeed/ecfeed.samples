package pages;

import org.openqa.selenium.WebDriver;

public class SearchPage extends MainPage{

	private final String fSearchUrl;
	
	public SearchPage(WebDriver driver, String searchString){
		super(driver);
		if(searchString.equals("")){
			fSearchUrl = tools.PageAddress.BASE;
		} else{
			fSearchUrl = tools.PageAddress.SEARCH_RESULTS;
		}
		initialiseElements();
	}
	
	@Override
	public boolean isLoaded(){
		String currentUrl = fDriver.getCurrentUrl();
		return currentUrl.contains(fSearchUrl);
	}
	


}
