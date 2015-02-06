package pages;

import org.openqa.selenium.WebDriver;

import pages.patterns.PageObject;

public class AccountPage extends PageObject{

	public AccountPage(WebDriver driver){
		super(driver, tools.PageAddress.ACCOUNT_INFO);
	}

	@Override
	protected void initialiseElements(){
		// TODO Auto-generated method stub
		
	}

}
