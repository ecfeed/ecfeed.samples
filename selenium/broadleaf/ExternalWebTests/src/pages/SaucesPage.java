package pages;

import org.openqa.selenium.WebDriver;

import pages.patterns.PageObject;

public class SaucesPage extends PageObject{
	
	public SaucesPage(WebDriver driver){
		super(driver, tools.PageAddress.SAUCES);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialiseElements(){
		// TODO Auto-generated method stub
		
	}

}
