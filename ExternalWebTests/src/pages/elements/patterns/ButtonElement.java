package pages.elements.patterns;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class ButtonElement extends AbstractWebElement{

	public ButtonElement(WebElement element){
		super(element);
	}

	public void pressWithEnter(){
		fElement.sendKeys(Keys.ENTER);
	}

	public void click(){
		fElement.click();
	}

}
