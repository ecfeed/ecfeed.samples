package pages.patterns;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import pages.elements.patterns.AbstractWebElement;

public abstract class PageObject{
	protected final WebDriver fDriver;
	protected final String	fUrl;
	
	protected final HashMap<String, AbstractWebElement> fElements;
	
	public PageObject(WebDriver driver, String url){
		this.fDriver = driver;
		this.fUrl = url;
		this.fElements = new HashMap<String, AbstractWebElement>();
	}
	
	public void get(){
		fDriver.get(fUrl);
	}
	
	public boolean isLoaded() throws Error{
		String currentUrl = fDriver.getCurrentUrl();
		return fUrl.equalsIgnoreCase(currentUrl);
	}
	
	public AbstractWebElement getElement(String fieldName){
		AbstractWebElement element = fElements.get(fieldName);
		if(element != null){
			return element;
		} else {
			throw new Error("Invalid element: " + fieldName);
		}
	}
	
	protected boolean isElementPresent(By by){
		try{
			fDriver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}
	
	protected boolean pageContains(String text){
		return (isElementPresent(By.xpath("//*[contains(.,'" + text + "')]")));
	}
	
	protected abstract void initialiseElements();

}
