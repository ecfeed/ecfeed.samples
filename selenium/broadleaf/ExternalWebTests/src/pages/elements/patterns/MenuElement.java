package pages.elements.patterns;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class MenuElement extends AbstractWebElementContainer{

	protected WebDriver fDriver;
	
	public MenuElement(WebElement element, WebDriver driver){
		super(element);
		fDriver = driver;
	}

}
