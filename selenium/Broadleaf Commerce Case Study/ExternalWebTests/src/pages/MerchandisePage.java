package pages;

import org.openqa.selenium.WebDriver;

import pages.patterns.PageObject;

public class MerchandisePage extends PageObject{

	public MerchandisePage(WebDriver driver){
		super(driver, tools.PageAddress.MERCHANDISE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialiseElements(){
		// TODO Auto-generated method stub
		
	}

}
